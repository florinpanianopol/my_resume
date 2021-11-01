package com.myresume.admin.educationSection.controller;

import com.myresume.admin.educationSection.EducationSectionNotFoundException;
import com.myresume.admin.educationSection.EducationSectionService;
import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.common.entity.EducationSection;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
public class EducationSectionController {

    @Autowired
    private EducationSectionService service;

    @GetMapping("/education_section")
    public String listFirstPage(Model model,@AuthenticationPrincipal MyResumeUserDetails loggedUser) {
        List<EducationSection> listEducationRecords = service.listAll();
        model.addAttribute("listEducationRecords", listEducationRecords);

        getNoOfCols(model, loggedUser, listEducationRecords);

        return listByPage(1, model, "institutionName", "asc", null, loggedUser);
    }

    private void getNoOfCols(Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser, List<EducationSection> listEducationRecords) {
        int noOfCol = 0;
        if (listEducationRecords.size() > 0) {
            noOfCol = listEducationRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<EducationSection> listActiveEducationRecords = service.findAllActiveRecords(loggedUser.getId());
        int activeRecordsCount = listActiveEducationRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);
    }

    @GetMapping("education_section/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword, @AuthenticationPrincipal MyResumeUserDetails loggedUser) {


        Page<EducationSection> page = service.listByPage(pageNum, sortField, sortDir, keyword,loggedUser.getId());
        List<EducationSection> listEducationRecords = page.getContent();

        getNoOfCols(model, loggedUser, listEducationRecords);

        long startCount = (pageNum - 1) * EducationSectionService.SECTIONS_PER_PAGE + 1;
        long endCount = startCount + EducationSectionService.SECTIONS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listEducationRecords", listEducationRecords);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "educationSection/education_section";
    }


    @GetMapping("/education_section/new")
    public String newEducation(Model model) {


        EducationSection educationSection = new EducationSection();
        educationSection.setEnabled(true);

        model.addAttribute("educationSection",educationSection);
        model.addAttribute("pageTitle","Add New Education");
        return "educationSection/education_section_form";
    }

    private void dateDifference(EducationSection educationSection) {
        if (educationSection.getToDate().toLocalDate().getYear() == 2099) {
            educationSection.setToDate(java.sql.Date.valueOf(LocalDate.now()));
        }
        LocalDate firstDate = LocalDate.of(educationSection.getToDate().toLocalDate().getYear(), educationSection.getToDate().toLocalDate().getMonth(), educationSection.getToDate().toLocalDate().getDayOfMonth());
        LocalDate secondDate = LocalDate.of(educationSection.getFromDate().toLocalDate().getYear(), educationSection.getFromDate().toLocalDate().getMonth(), educationSection.getFromDate().toLocalDate().getDayOfMonth());
        Period period = Period.between(secondDate, firstDate);

        String year = period.getYears() + " year";
        if (period.getYears() > 1) {
            year = period.getYears() + " years";
        } else if (period.getYears() < 1) {
            year = "";
        }

        String month;
        if (period.getMonths() > 1 && period.getYears() >= 1) {
            month = ", " + period.getMonths() + " months";
        } else if (period.getMonths() == 1 && period.getYears() >= 1) {
            month = ", " + period.getMonths() + " month";
        } else if (period.getMonths() > 1) {
            month = period.getMonths() + " months";
        } else if (period.getMonths() == 1) {
            month = period.getMonths() + " month";
        } else {
            month = "";
        }
        if (year.isEmpty() && month.isEmpty()) {
            month = "< 1 month";
        }

        educationSection.setDateDiff(year + month);

    }

    @PostMapping("/education_section/save")
    public String saveEducation(@ModelAttribute("educationSection") @Valid EducationSection educationSection, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                            Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {

        boolean existingEducationFlag = false;

        String target = "";
        int errorCountFix = 0;

        if (educationSection.getFromDate() == null && educationSection.getToDate() != null) {
            ObjectError error = new ObjectError("fromDateError", "- Can't be null");
            bindingResult.addError(error);
            model.addAttribute("fromDateError", error.getDefaultMessage());
            errorCountFix=1;
        } else if (educationSection.getToDate() == null &&educationSection.getFromDate() !=null) {
            ObjectError error = new ObjectError("toDateError", "- Can't be null");
            bindingResult.addError(error);
            model.addAttribute("toDateError", error.getDefaultMessage());
            errorCountFix=1;
        } else if (educationSection.getToDate() == null && educationSection.getFromDate() == null) {
            ObjectError errorToDate = new ObjectError("toDateError", "- Can't be null");
            ObjectError errorFromDate = new ObjectError("fromDateError", "- Can't be null");
            bindingResult.addError(errorToDate);
            bindingResult.addError(errorFromDate);
            model.addAttribute("toDateError", errorToDate.getDefaultMessage());
            model.addAttribute("fromDateError", errorFromDate.getDefaultMessage());
            errorCountFix=2;
        }
        else if (educationSection.getFromDate() != null && educationSection.getFromDate().after(educationSection.getToDate())) {
            ObjectError error = new ObjectError("fromDateError", "- from date can't be greater than to Date.");
            bindingResult.addError(error);
            model.addAttribute("fromDateError", error.getDefaultMessage());
        }
        else {
            dateDifference(educationSection);
        }

        List<EducationSection> listEducations = service.listAll();
        model.addAttribute("educationSection", educationSection);
        educationSection.setUser_id(loggedUser.getId());


        for(int i =0;i<listEducations.size();i++){
            if(listEducations.get(i).getInstitutionName().toLowerCase().equals(educationSection.getInstitutionName().toLowerCase())
                    && listEducations.get(i).getProgramType().toLowerCase().equals(educationSection.getProgramType().toLowerCase())
                    && listEducations.get(i).getUser_id()==(loggedUser.getId())
                    &&listEducations.get(i).getId()!= educationSection.getId()
            ){
                existingEducationFlag=true;
                break;
            }
        }

        if(existingEducationFlag){
            ObjectError error = new ObjectError("artificialBindingError", "artificialBindingError");
            bindingResult.addError(error);
            model.addAttribute("uniquenessMessage","The institution & program combo must be unique!");
        }

        if (bindingResult.hasErrors()) {
            int errorCount = bindingResult.getAllErrors().size()-errorCountFix;
            if(errorCount>1) {
                model.addAttribute("message", "You have "+errorCount+" errors to address!");
            }
            else
            {
                model.addAttribute("message", "You have "+errorCount+" error to address!");
            }
            target = "educationSection/education_section_form";
        } else {

            service.save(educationSection);
            redirectAttributes.addFlashAttribute("message", "The education has been saved successfully");
            String educationNamePart = educationSection.getInstitutionName();
            target = "redirect:/education_section/page/1?sortField=id&sortDir=asc&keyword=" + educationNamePart;
    }
        return target;
}

    @GetMapping("/education_section/{id}/enabled/{status}")
    public String updateEducationEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes ) {
        service.updateEnabled(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The education ID "+id+ " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/education_section";
    }

    @GetMapping("/education_section/delete/{id}")
    public String deleteEducation(@PathVariable(name="id") Integer id, Model model,
                              RedirectAttributes redirectAttributes
    )  {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The education ID "+ id +" has been deleted successfully");

        } catch (EducationSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message","din delete "+ ex.getMessage());

        }
        return "redirect:/education_section";

    }

    @GetMapping("/education_section/edit/{id}")
    public String editEducationSection(@PathVariable(name = "id") Integer id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        try {
            EducationSection educationSection = service.get(id);


            model.addAttribute("educationSection", educationSection);
            model.addAttribute("pageTitle", "Edit Education section");
            return "educationSection/education_section_form";

        } catch (EducationSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/education_section";
        }


    }
}
