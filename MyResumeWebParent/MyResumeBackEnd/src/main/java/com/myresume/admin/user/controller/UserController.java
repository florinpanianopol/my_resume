package com.myresume.admin.user.controller;


import com.myresume.admin.FileUploadUtil;
import com.myresume.admin.security.MyResumeUserDetails;
import com.myresume.admin.user.UserNotFoundException;
import com.myresume.admin.user.UserService;
import com.myresume.common.entity.Role;
import com.myresume.common.entity.User;
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
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);

        int noOfCol = 0;
        if (listUsers.size() > 0) {
            noOfCol = listUsers.get(0).getClass().getDeclaredFields().length;
        }

        List<User> listActiveUsers = service.findAllActiveRecords();
        int activeRecordsCount = listActiveUsers.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);

        return listByPage(1,model,"firstName","asc",null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword
    ) {
        Page<User> page = service.listByPage(pageNum, sortField, sortDir,keyword);
        List<User> listUsers = page.getContent();

        int noOfCol = 0;
        if (listUsers.size() > 0) {
            noOfCol = listUsers.get(0).getClass().getDeclaredFields().length;
        }

        List<User> listActiveUsers = service.findAllActiveRecords();
        int activeRecordsCount = listActiveUsers.size();

        model.addAttribute("noOfCol", noOfCol);
        model.addAttribute("activeRecordsCount", activeRecordsCount);

//		System.out.print("PageNum = "+pageNum);
//		System.out.print("Total elements = "+page.getTotalElements());
//		System.out.print("Total pages = "+page.getTotalPages());

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE+1;
        long endCount = startCount + UserService.USERS_PER_PAGE-1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "users/users";

    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = service.listRoles();

        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Create New User");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile, Model model,@AuthenticationPrincipal MyResumeUserDetails loggedUser
    ) throws IOException {
        boolean passwordFlag = false;
        boolean userExistingFlag = false;

        List<User> listUsers = service.listAll();
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles",listRoles);

        for(int i =0;i<listUsers.size();i++){
            if(listUsers.get(i).getId().equals(user.getId())){
                userExistingFlag=true;
                break;
            }
        }

        if(user.getPassword().trim().length()<8 &&!userExistingFlag){
            ObjectError error = new ObjectError("artificialBindingError", "artificialBindingError");
            bindingResult.addError(error);
            passwordFlag=true;
        }

        if (bindingResult.hasErrors()) {
            if(passwordFlag) {
                model.addAttribute("password_message", "The password must be greater than 8 characters!");
            }
        return "users/user_form";
        }


        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
//            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);


        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }

        if(user.getEmail().equals(loggedUser.getUsername())) {
            loggedUser.setFirstName(user.getFirstName());
            loggedUser.setLastName(user.getLastName());
            loggedUser.setPhotos(user.getPhotos());
        }


        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");

        return getRedirectURLtoAffectedUser(user);

    }

    private String getRedirectURLtoAffectedUser(User user) {
        String email=user.getEmail();
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" +email;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name="id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        try {
            User user = service.get(id);
            List<Role> listRoles = service.listRoles();

            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit User with ID: " + id);
            model.addAttribute("listRoles",listRoles);
            return "users/user_form";

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message","din edit "+ ex.getMessage());
            return "redirect:/users";
        }


    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id") Integer id,Model model,
                             RedirectAttributes redirectAttributes
    )  {

        try {
            service.delete(id);
            String uploadDir = "user-photos/" + id;
            File file = new File("user-photos/" + id);
//            FileUploadUtil.removeDir(uploadDir);
            FileUtils.deleteDirectory(file);
            redirectAttributes.addFlashAttribute("message","The user ID "+ id +" has been deleted successfully");

        } catch (UserNotFoundException | IOException ex) {
            redirectAttributes.addFlashAttribute("message","din delete "+ ex.getMessage());

        }
        return "redirect:/users";

    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes ) {
        service.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID "+id+ " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }

//    @GetMapping("/users/export/csv")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        List<User> listUsers = service.listAll();
//        UserCsvExporter exporter = new UserCsvExporter();
//        exporter.export(listUsers, response);
//    }
//
//    @GetMapping("/users/export/excel")
//    public void exportToExcel(HttpServletResponse response) throws IOException {
//        List<User> listUsers = service.listAll();
//        UserExcelExporter exporter = new UserExcelExporter();
//        exporter.export(listUsers, response);
//
//    }
//
//    @GetMapping("/users/export/pdf")
//    public void exportToPDF(HttpServletResponse response) throws IOException {
//        List<User> listUsers = service.listAll();
//        UserPDFExporter exporter = new UserPDFExporter();
//        exporter.export(listUsers, response);
//
//    }


//    public static void removeDirectory(File dir) throws InterruptedException {
//
//        if (dir.isDirectory()) {
//            File[] files = dir.listFiles();
//            if (files != null && files.length > 0) {
//
//                for (File aFile : files) {
//                    removeDirectory(aFile);
//                }
//            }
//            dir.delete();
//
//        } else {
//
//            dir.delete();
//
//        }
//    }
}

