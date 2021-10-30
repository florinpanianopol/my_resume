package com.myresume.admin.workSection.controller;

import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.admin.workSection.WorkSectionNotFoundException;
import com.myresume.admin.workSection.WorkSectionService;
import com.myresume.common.entity.WorkSection;
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
public class WorkSectionController {

    @Autowired
    private WorkSectionService service;

    @GetMapping("/work_section")
    public String listFirstPage(Model model,@AuthenticationPrincipal MyResumeUserDetails loggedUser) {
        List<WorkSection> listWorkSectionRecords = service.listAll();
        model.addAttribute("listWorkSectionRecords", listWorkSectionRecords);

        getNoOfCols(model, loggedUser, listWorkSectionRecords);

        return listByPage(1, model, "jobName", "asc", null, loggedUser);
    }

    private void getNoOfCols(Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser, List<WorkSection> listWorkSectionRecords) {
        int noOfCol = 0;
        if (listWorkSectionRecords.size() > 0) {
            noOfCol = listWorkSectionRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<WorkSection> listActiveWorkSectionRecords = service.findAllActiveRecords(loggedUser.getId());
        int activeRecordsCount = listActiveWorkSectionRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);
        model.addAttribute("listActiveWorkSectionRecords", listActiveWorkSectionRecords);
    }

    @GetMapping("work_section/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword, @AuthenticationPrincipal MyResumeUserDetails loggedUser) {


        Page<WorkSection> page = service.listByPage(pageNum, sortField, sortDir, keyword,loggedUser.getId());
        List<WorkSection> listWorkSectionRecords = page.getContent();

        getNoOfCols(model, loggedUser, listWorkSectionRecords);

        long startCount = (pageNum - 1) * WorkSectionService.SECTIONS_PER_PAGE + 1;
        long endCount = startCount + WorkSectionService.SECTIONS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listWorkSectionRecords", listWorkSectionRecords);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "workSection/work_section";
    }


    @GetMapping("/work_section/new")
    public String newWorkSection(Model model) {


        WorkSection workSection = new WorkSection();
        workSection.setEnabled(true);

        model.addAttribute("workSection",workSection);
        model.addAttribute("pageTitle","Add New work section");
        return "workSection/work_section_form";
    }

    private void dateDifference(WorkSection workSection) {
        if (workSection.getToDate().toLocalDate().getYear() == 2099) {
            workSection.setToDate(java.sql.Date.valueOf(LocalDate.now()));
        }
        LocalDate firstDate = LocalDate.of(workSection.getToDate().toLocalDate().getYear(), workSection.getToDate().toLocalDate().getMonth(), workSection.getToDate().toLocalDate().getDayOfMonth());
        LocalDate secondDate = LocalDate.of(workSection.getFromDate().toLocalDate().getYear(), workSection.getFromDate().toLocalDate().getMonth(), workSection.getFromDate().toLocalDate().getDayOfMonth());
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

        workSection.setDateDiff(year + month);
    }

    @PostMapping("/work_section/save")
    public String saveWorkSection(@ModelAttribute("workSection") @Valid WorkSection workSection, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                            Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {

        dateDifference(workSection);

        model.addAttribute("workSection", workSection);
        model.addAttribute("pageTitle", "Create New Work Section Record");
        workSection.setUser_id(loggedUser.getId());

        int errorCountFix = 0;
        if (workSection.getFromDate() == null && workSection.getToDate() != null) {
            ObjectError error = new ObjectError("fromDateError", "- Can't be null");
            bindingResult.addError(error);
            model.addAttribute("fromDateError", error.getDefaultMessage());
            errorCountFix=1;
        } else if (workSection.getToDate() == null &&workSection.getFromDate() !=null) {
            ObjectError error = new ObjectError("toDateError", "- Can't be null");
            bindingResult.addError(error);
            model.addAttribute("toDateError", error.getDefaultMessage());
            errorCountFix=1;
        } else if (workSection.getToDate() == null && workSection.getFromDate() == null) {
            ObjectError errorToDate = new ObjectError("toDateError", "- Can't be null");
            ObjectError errorFromDate = new ObjectError("fromDateError", "- Can't be null");
            bindingResult.addError(errorToDate);
            bindingResult.addError(errorFromDate);
            model.addAttribute("toDateError", errorToDate.getDefaultMessage());
            model.addAttribute("fromDateError", errorFromDate.getDefaultMessage());
            errorCountFix=2;
        }
        else {
            assert workSection.getFromDate() != null;
            if (workSection.getFromDate().after(workSection.getToDate())) {
                ObjectError error = new ObjectError("fromDateError", "- from date can't be greater than to Date.");
                bindingResult.addError(error);
                model.addAttribute("fromDateError", error.getDefaultMessage());
            }
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
            return "workSection/work_section_form";
        }

        workSection.setUser_id(loggedUser.getId());
        service.save(workSection);

        redirectAttributes.addFlashAttribute("message","The work section has been saved successfully");
        return getRedirectURLtoAffectedUser(workSection);

    }

    private String getRedirectURLtoAffectedUser(WorkSection workSection) {
        String jobName=workSection.getJobName();
        return "redirect:/work_section/page/1?sortField=id&sortDir=asc&keyword=" +jobName;
    }

    @GetMapping("/work_section/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean currInd, RedirectAttributes redirectAttributes) {

            service.updateEnabled(id, currInd);
            String status = currInd ? "enabled" : "disabled";
            String message = "The work section section with ID " + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);


        return "redirect:/work_section";
    }

    @GetMapping("/work_section/delete/{id}")
    public String deleteWorkSection(@PathVariable(name="id") Integer id,Model model,
                              RedirectAttributes redirectAttributes
    )  {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The workSection ID "+ id +" has been deleted successfully");

        } catch (WorkSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message","din delete "+ ex.getMessage());

        }
        return "redirect:/work_section";

    }

    @GetMapping("/work_section/edit/{id}")
    public String editWorkSection(@PathVariable(name = "id") Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        try {
            WorkSection workSection = service.get(id);


            model.addAttribute("workSection", workSection);
            model.addAttribute("pageTitle", "Edit work section with ID: " + id);
            return "workSection/work_section_form";

        } catch (WorkSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/work_section";
        }


    }
}
