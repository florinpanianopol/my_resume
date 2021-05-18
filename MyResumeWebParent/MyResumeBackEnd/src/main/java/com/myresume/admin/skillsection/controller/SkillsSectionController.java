package com.myresume.admin.skillsection.controller;

import com.myresume.admin.aboutsection.AboutSectionNotFoundException;
import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.admin.skillsection.SkillsSectionNotFoundException;
import com.myresume.admin.skillsection.SkillsSectionService;
import com.myresume.common.entity.SkillsSection;
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
public class SkillsSectionController {

    @Autowired
    private SkillsSectionService service;

    @GetMapping("/skills_section")
    public String listFirstPage(Model model,@AuthenticationPrincipal MyResumeUserDetails loggedUser) {
        List<SkillsSection> listSkillsRecords = service.listAll();
        model.addAttribute("listSkillsRecords", listSkillsRecords);

        getNoOfCols(model, loggedUser, listSkillsRecords);

        return listByPage(1, model, "skillCategory", "asc", null, loggedUser);
    }

    private void getNoOfCols(Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser, List<SkillsSection> listSkillsRecords) {
        int noOfCol = 0;
        if (listSkillsRecords.size() > 0) {
            noOfCol = listSkillsRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<SkillsSection> listActiveSkillsRecords = service.findAllActiveRecords(loggedUser.getId());
        int activeRecordsCount = listActiveSkillsRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);
    }

    @GetMapping("skills_section/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,@AuthenticationPrincipal MyResumeUserDetails loggedUser) {


        Page<SkillsSection> page = service.listByPage(pageNum, sortField, sortDir, keyword,loggedUser.getId());
        List<SkillsSection> listSkillsRecords = page.getContent();

        getNoOfCols(model, loggedUser, listSkillsRecords);

        long startCount = (pageNum - 1) * SkillsSectionService.SKILLS_PER_PAGE + 1;
        long endCount = startCount + SkillsSectionService.SKILLS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSkillsRecords", listSkillsRecords);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "skillsSection/skills_section";
    }


    @GetMapping("/skills_section/new")
    public String newSkill(Model model) {


        SkillsSection skillsSection = new SkillsSection();
        skillsSection.setEnabled(true);

        model.addAttribute("skillsSection",skillsSection);
        model.addAttribute("pageTitle","Add New Skill");
        return "skillsSection/skills_section_form";
    }

    @PostMapping("/skills_section/save")
    public String saveSkill(@ModelAttribute("skillsSection") @Valid SkillsSection skillsSection, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                         Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {

        boolean existingSkillFlag = false;

        List<SkillsSection> listSkills = service.listAll();
        model.addAttribute("skillSection", skillsSection);

        for(int i =0;i<listSkills.size();i++){
            if(listSkills.get(i).getSkillTitle().toLowerCase().equals(skillsSection.getSkillTitle().toLowerCase())&& listSkills.get(i).getUser_id()==(loggedUser.getId())
            &&listSkills.get(i).getId()!=skillsSection.getId()
            ){
                existingSkillFlag=true;
                break;
            }
        }

        if(existingSkillFlag){
            ObjectError error = new ObjectError("artificialBindingError", "artificialBindingError");
            bindingResult.addError(error);
            model.addAttribute("skills_message","The skill must be unique");
        }

        if (bindingResult.hasErrors()) {
            return "skillsSection/skills_section_form";
        }


            skillsSection.setUser_id(loggedUser.getId());
            service.save(skillsSection);

        //for updating the profile photo of the logged user
//        if(user.getEmail().equals(loggedUser.getUsername())) {
//            loggedUser.setFirstName(user.getFirstName());
//            loggedUser.setLastName(user.getLastName());
//            loggedUser.setPhotos(user.getPhotos());
//        }


        redirectAttributes.addFlashAttribute("message","The skill has been saved successfully");

        return getRedirectURLtoAffectedUser(skillsSection);

    }

    private String getRedirectURLtoAffectedUser(SkillsSection skillsSection) {
        String skillTitle=skillsSection.getSkillTitle();
        return "redirect:/skills_section/page/1?sortField=id&sortDir=asc&keyword=" +skillTitle;
    }

    @GetMapping("/skills_section/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes ) {
        service.updateEnabled(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The skill ID "+id+ " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/skills_section";
    }

    @GetMapping("/skills_section/delete/{id}")
    public String deleteSkill(@PathVariable(name="id") Integer id,Model model,
                             RedirectAttributes redirectAttributes
    )  {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The skill ID "+ id +" has been deleted successfully");

        } catch (SkillsSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message","din delete "+ ex.getMessage());

        }
        return "redirect:/skills_section";

    }

    @GetMapping("/skills_section/edit/{id}")
    public String editAboutSection(@PathVariable(name = "id") Integer id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        try {
            SkillsSection skillsSection = service.get(id);


            model.addAttribute("skillsSection", skillsSection);
            model.addAttribute("pageTitle", "Edit skills section with ID: " + id);
            return "skillsSection/skills_section_form";

        } catch (AboutSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/skills_section";
        }


    }

}
