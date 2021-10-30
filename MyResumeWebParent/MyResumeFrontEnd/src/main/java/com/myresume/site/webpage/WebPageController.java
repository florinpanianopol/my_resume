package com.myresume.site.webpage;

import com.myresume.admin.aboutsection.AboutSectionService;
import com.myresume.admin.languageSection.LanguageSectionService;
import com.myresume.admin.skillsection.SkillsSectionService;
import com.myresume.admin.user.UserService;
import com.myresume.admin.workSection.WorkSectionService;
import com.myresume.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebPageController {

	@Autowired
	private AboutSectionService aboutService;

	@Autowired
	private UserService userService;

	@Autowired
	private SkillsSectionService skillService;

	@Autowired
	private LanguageSectionService languageService;

	@Autowired
	private WorkSectionService workSectionService;

	@GetMapping("/")
	public String findAllActiveRecords(Model model) {

		List<User> listUsers = userService.listAll().stream()
				.filter(p -> p.isEnabled()).collect(Collectors.toList());

		List<AboutSection> listAboutRecords = aboutService.listAll().stream()
				.filter(p -> p.getCurrInd()&& p.getUser_id().equals(listUsers.get(0).getId())).collect(Collectors.toList());

		List<SkillsSection> listSkillRecords = skillService.listAll().stream()
				.filter(p -> p.isEnabled()&& p.getUser_id().equals(listUsers.get(0).getId())).collect(Collectors.toList());

		List<LanguageSection> listLanguageRecords = languageService.listAll().stream()
				.filter(p -> p.isEnabled()&& p.getUser_id().equals(listUsers.get(0).getId())).collect(Collectors.toList());

		List<WorkSection> listWorkRecords = workSectionService.listAll().stream()
				.filter(p -> p.isEnabled()&& p.getUser_id().equals(listUsers.get(0).getId())).collect(Collectors.toList());

		Set<String> distinctCategories = new HashSet<String>();
		for(int i=0;i<listSkillRecords.size();i++){
			distinctCategories.add(listSkillRecords.get(i).getSkillCategory());
		}


		model.addAttribute("distinctCategories",distinctCategories);
		model.addAttribute("listAboutRecords",listAboutRecords);
		model.addAttribute("listWorkSectionRecords",listWorkRecords);
		model.addAttribute("listSkillRecords",listSkillRecords);
		model.addAttribute("listLanguageRecords",listLanguageRecords);
		model.addAttribute("listWorkRecords",listWorkRecords);

		return "home";
	}
}