package com.myresume.admin.workSection.controller;

import com.myresume.admin.FileUploadUtil;
import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.admin.workSection.WorkSectionNotFoundException;
import com.myresume.admin.workSection.WorkSectionService;
import com.myresume.common.entity.WorkSection;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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

        int days = (int) ChronoUnit.DAYS.between(secondDate, firstDate);
        workSection.setDateDiff(year + month);
        workSection.setDaysDiff(days);
    }

    @PostMapping("/work_section/save")
    public String saveWorkSection(@ModelAttribute("workSection") @Valid WorkSection workSection, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("image") MultipartFile multipartFile,
                                  Model model, @AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {

        String target = "";
        int errorCountFix = 0;
        boolean existingEducationFlag = false;

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
        else if (workSection.getFromDate() != null && workSection.getFromDate().after(workSection.getToDate())) {
                ObjectError error = new ObjectError("fromDateError", "- from date can't be greater than to Date.");
                bindingResult.addError(error);
                model.addAttribute("fromDateError", error.getDefaultMessage());
            }
        else {
            dateDifference(workSection);
        }


        List<WorkSection> listWorkSections = service.findAllActiveRecords(loggedUser.getId());
        model.addAttribute("workSection", workSection);
        model.addAttribute("pageTitle", "Add New work section");
        workSection.setUser_id(loggedUser.getId());


        for(int i =0;i<listWorkSections.size();i++){
            if(listWorkSections.get(i).getCompany().toLowerCase().equals(workSection.getCompany().toLowerCase())
                    && listWorkSections.get(i).getJobName().toLowerCase().equals(workSection.getJobName().toLowerCase())
                    &&listWorkSections.get(i).getId()!= workSection.getId()
            ){
                existingEducationFlag=true;
                break;
            }
        }

        if(existingEducationFlag){
            model.addAttribute("uniquenessMessage","The job name & company combo must be unique!");
            return "workSection/work_section_form";
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
            target = "workSection/work_section_form";
        } else {

            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                workSection.setCompanyLogoPhoto(fileName);
                WorkSection savedSection = service.save(workSection);
                String uploadDir = "user_company_logos/" + savedSection.getId();

                try {
                    File file = new File("user_company_logos/" + savedSection.getId());
                    FileUtils.cleanDirectory(file);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

//                FileUploadUtil.cleanDir(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);


            } else {
                if (workSection.getCompanyLogoPhoto().isEmpty()) workSection.setCompanyLogoPhoto(null);
                service.save(workSection);
            }

            redirectAttributes.addFlashAttribute("message", "The record has been saved successfully");
            String jobNamePart = workSection.getJobName();
            target = "redirect:/work_section/page/1?sortField=id&sortDir=asc&keyword=" + jobNamePart;
        }
        return target;
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
            File file = new File("user_company_logos/" + id);
            FileUtils.deleteDirectory(file);
            redirectAttributes.addFlashAttribute("message","The workSection ID "+ id +" has been deleted successfully");

        } catch (WorkSectionNotFoundException | IOException ex) {
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
            model.addAttribute("pageTitle", "Edit work section");
            return "workSection/work_section_form";

        } catch (WorkSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/work_section";
        }


    }
}
