package com.myresume.site.webpage;

import com.myresume.admin.section.AboutSectionService;
import com.myresume.common.entity.AboutSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebPageController {

	@Autowired
	private AboutSectionService service;
	
	@GetMapping("/")
	public String findAllActiveRecords(Model model) {
		List<AboutSection> listAboutRecords = service.findAllActiveRecords();
		model.addAttribute("listAboutRecords",listAboutRecords);
		
		for(int i =0;i<listAboutRecords.size();i++) {
		System.out.print(listAboutRecords.get(i).getHeader());
		}
		return "home";
	}
}