package com.myresume.admin.languageSection.controller;

import com.myresume.admin.languageSection.LanguageSectionNotFoundException;
import com.myresume.admin.languageSection.LanguageSectionService;
import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.common.entity.LanguageSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class LanguageSectionController {

    @Autowired
    private LanguageSectionService service;

    @GetMapping("/language_section")
    public String listFirstPage(Model model,@AuthenticationPrincipal MyResumeUserDetails loggedUser) {
        List<LanguageSection> listLanguageRecords = service.listAll();
        model.addAttribute("listLanguageRecords", listLanguageRecords);

        getNoOfCols(model, loggedUser, listLanguageRecords);

        return listByPage(1, model, "language", "asc", null, loggedUser);
    }

    private void getNoOfCols(Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser, List<LanguageSection> listLanguageRecords) {
        int noOfCol = 0;
        if (listLanguageRecords.size() > 0) {
            noOfCol = listLanguageRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<LanguageSection> listActiveLanguageRecords = service.findAllActiveRecords(loggedUser.getId());
        int activeRecordsCount = listActiveLanguageRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);
    }

    @GetMapping("language_section/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword, @AuthenticationPrincipal MyResumeUserDetails loggedUser) {


        Page<LanguageSection> page = service.listByPage(pageNum, sortField, sortDir, keyword,loggedUser.getId());
        List<LanguageSection> listLanguageRecords = page.getContent();

        getNoOfCols(model, loggedUser, listLanguageRecords);

        long startCount = (pageNum - 1) * LanguageSectionService.LANGUAGES_PER_PAGE + 1;
        long endCount = startCount + LanguageSectionService.LANGUAGES_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listLanguageRecords", listLanguageRecords);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "languageSection/language_section";
    }


    @GetMapping("/language_section/new")
    public String newLanguage(Model model) {


        LanguageSection languageSection = new LanguageSection();
        languageSection.setEnabled(true);

        model.addAttribute("languageSection",languageSection);
        model.addAttribute("pageTitle","Add New Language");
        return "languageSection/language_section_form";
    }

    @PostMapping("/language_section/save")
    public String saveLanguage(@ModelAttribute("languageSection") @Valid LanguageSection languageSection, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                            Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {

        boolean existingLanguageFlag = false;

        List<LanguageSection> listLanguages = service.listAll();
        model.addAttribute("languageSection", languageSection);

        for(int i =0;i<listLanguages.size();i++){
            if(listLanguages.get(i).getLanguage().toLowerCase().equals(languageSection.getLanguage().toLowerCase())&& listLanguages.get(i).getUser_id()==(loggedUser.getId())
                    &&listLanguages.get(i).getId()!=languageSection.getId()
            ){
                existingLanguageFlag=true;
                break;
            }
        }

        if(existingLanguageFlag){
            ObjectError error = new ObjectError("artificialBindingError", "artificialBindingError");
            bindingResult.addError(error);
            model.addAttribute("language_message","The language must be unique");
        }

        if (bindingResult.hasErrors()) {
            return "languageSection/language_section_form";
        }


        languageSection.setUser_id(loggedUser.getId());
        service.save(languageSection);

        //for updating the profile photo of the logged user
//        if(user.getEmail().equals(loggedUser.getUsername())) {
//            loggedUser.setFirstName(user.getFirstName());
//            loggedUser.setLastName(user.getLastName());
//            loggedUser.setPhotos(user.getPhotos());
//        }


        redirectAttributes.addFlashAttribute("message","The language has been saved successfully");

        return getRedirectURLtoAffectedUser(languageSection);

    }

    private String getRedirectURLtoAffectedUser(LanguageSection languageSection) {
        String languageTitle=languageSection.getLanguage();
        return "redirect:/language_section/page/1?sortField=id&sortDir=asc&keyword=" +languageTitle;
    }

    @GetMapping("/language_section/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes ) {
        service.updateEnabled(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The language ID "+id+ " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/language_section";
    }

    @GetMapping("/language_section/delete/{id}")
    public String deleteLanguage(@PathVariable(name="id") Integer id, Model model,
                              RedirectAttributes redirectAttributes
    )  {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The language ID "+ id +" has been deleted successfully");

        } catch (LanguageSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message","din delete "+ ex.getMessage());

        }
        return "redirect:/language_section";

    }

    @GetMapping("/language_section/edit/{id}")
    public String editLanguageSection(@PathVariable(name = "id") Integer id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        try {
            LanguageSection languageSection = service.get(id);


            model.addAttribute("languageSection", languageSection);
            model.addAttribute("pageTitle", "Edit language section with ID: " + id);
            return "languageSection/language_section_form";

        } catch (LanguageSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/language_section";
        }


    }
}
