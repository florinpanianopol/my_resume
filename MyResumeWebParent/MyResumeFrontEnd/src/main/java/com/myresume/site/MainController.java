package com.myresume.site;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import com.myresume.admin.section.AboutSectionService;
//import com.myresume.common.entity.AboutSection;

@Controller
@ComponentScan({"com.myresume.common.entity", "com.myresume.admin.aboutsection","com.myresume.admin.skillsection","com.myresume.admin.languageSection",
		"com.myresume.admin.workSection","com.myresume.admin.user"})
@EntityScan({"com.myresume.common.entity","com.myresume.admin.section","com.myresume.admin.user"})
@EnableJpaRepositories({"com.myresume.admin.aboutsection", "com.myresume.admin.skillsection", "com.myresume.admin.user","com.myresume.admin.languageSection",
		"com.myresume.admin.workSection"})
@SpringBootApplication
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
}
