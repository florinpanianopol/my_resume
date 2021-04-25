package com.myresume.admin.aboutsection;

import com.myresume.admin.FileUploadUtil;
import com.myresume.admin.aboutsection.export.AboutSectionCsvExporter;
import com.myresume.admin.aboutsection.export.AboutSectionExcelExporter;
import com.myresume.admin.aboutsection.export.AboutSectionPDFExporter;
import com.myresume.common.entity.AboutSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class AboutSectionController {

    @Autowired
    private AboutSectionService service;


    @GetMapping("/about_section")
    public String listFirstPage(Model model) {
        List<AboutSection> listAboutRecords = service.listAll();
        model.addAttribute("listAboutRecords", listAboutRecords);

        int noOfCol = 0;
        if (listAboutRecords.size() > 0) {
            noOfCol = listAboutRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<AboutSection> listActiveAboutRecords = service.findAllActiveRecords();
        int activeRecordsCount = listActiveAboutRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);

        return listByPage(1, model, "name", "asc", null);
    }

    @GetMapping("about_section/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {


        Page<AboutSection> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<AboutSection> listAboutRecords = page.getContent();

        int noOfCol = 0;
        if (listAboutRecords.size() > 0) {
            noOfCol = listAboutRecords.get(0).getClass().getDeclaredFields().length;
        }


        List<AboutSection> listActiveAboutRecords = service.findAllActiveRecords();
        int activeRecordsCount = listActiveAboutRecords.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);

        long startCount = (pageNum - 1) * AboutSectionService.USERS_PER_PAGE + 1;
        long endCount = startCount + AboutSectionService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAboutRecords", listAboutRecords);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "about_section";
    }

    @GetMapping("/about_section/new")
    public String newRecord(Model model) {

        AboutSection aboutsection = new AboutSection();

        model.addAttribute("aboutsection", aboutsection);
        model.addAttribute("pageTitle", "Create New Record");
        return "about_section_form";
    }

    @PostMapping("/about_section/save")
    public String saveAboutSection(@ModelAttribute("aboutsection") @Valid AboutSection aboutsection, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam("image") MultipartFile multipartFile,
                                   Model model) throws IOException {

        String target = "";
        boolean flag = false;

        List<AboutSection> listAboutRecords = service.listAll();
        model.addAttribute("aboutsection", aboutsection);
        model.addAttribute("pageTitle", "Create New Record");

        for (int i = 0; i < listAboutRecords.size(); i++) {
            if (listAboutRecords.get(i).getCurrInd() && aboutsection.getCurrInd()
                    && listAboutRecords.get(i).getId() != aboutsection.getId()
            ) {
                ObjectError error = new ObjectError("currIndError", "There is already an Enabled record. Disable it first and then insert a new one!");
                bindingResult.addError(error);
                flag = true;
                break;
            }
        }


        if (bindingResult.hasErrors()) {
            if (flag) {
                model.addAttribute("message", "There is already an Enabled record. Disable it first and then insert a new one!");
            }
            target = "about_section_form";
        } else {

            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                aboutsection.setProfilePhoto(fileName);
                AboutSection savedSection = service.save(aboutsection);
                String uploadDir = "profile-photos/" + savedSection.getId();

                FileUploadUtil.cleanDir(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);


            } else {
                if (aboutsection.getProfilePhoto().isEmpty()) aboutsection.setProfilePhoto(null);
                service.save(aboutsection);
            }


            redirectAttributes.addFlashAttribute("message", "The record has been saved successfully");

            // de adaugat o coloana unica gen email pe care sa o inclidem la 165
            String emailPart = aboutsection.getEmail();
            target = "redirect:/about_section/page/1?sortField=name&sortDir=asc&keyword=" + emailPart;
        }
        return target;
    }


    @GetMapping("/about_section/edit/{id}")
    public String editAboutSection(@PathVariable(name = "id") Integer id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        try {
            AboutSection aboutsection = service.get(id);


            model.addAttribute("aboutsection", aboutsection);
            model.addAttribute("pageTitle", "Edit about section with ID: " + id);
            return "about_section_form";

        } catch (AboutSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/about_section";
        }


    }


    @GetMapping("/about_section/delete/{id}")
    public String deleteAboutSection(@PathVariable(name = "id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The about_section with ID " + id + " has been deleted successfully");

        } catch (AboutSectionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/about_section";
    }

    @GetMapping("/about_section/{id}/currInd/{status}")
    public String updateCurrentInd(@PathVariable("id") Integer id,
                                   @PathVariable("status") boolean currInd, RedirectAttributes redirectAttributes, AboutSection aboutsection,
                                   Model model) {


        boolean flag = false;

        List<AboutSection> listAboutRecords = service.listAll();
        model.addAttribute("aboutsection", aboutsection);

        for (int i = 0; i < listAboutRecords.size(); i++) {
            if (listAboutRecords.get(i).getCurrInd() && listAboutRecords.get(i).getId() != id
            ) {
                flag = true;
                break;
            }
        }

        if (flag) {
            redirectAttributes.addFlashAttribute("message", "There is already an Enabled record. Disable it first and then insert a new one!");
        } else {
            service.updateCurrentInd(id, currInd);
            String status = currInd ? "enabled" : "disabled";
            String message = "The about section with ID " + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        }

        return "redirect:/about_section";
    }


    @GetMapping("/about_section/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<AboutSection> listAboutSections = service.listAll();
        AboutSectionCsvExporter exporter = new AboutSectionCsvExporter();
        exporter.export(listAboutSections, response);
    }

    @GetMapping("/about_section/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<AboutSection> listAboutSections = service.listAll();

        AboutSectionExcelExporter exporter = new AboutSectionExcelExporter();
        exporter.export(listAboutSections, response);
    }

    @GetMapping("/about_section/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<AboutSection> listAboutSections = service.listAll();

        AboutSectionPDFExporter exporter = new AboutSectionPDFExporter();
        exporter.export(listAboutSections, response);
    }
}
