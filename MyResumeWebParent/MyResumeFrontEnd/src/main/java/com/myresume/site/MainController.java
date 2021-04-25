package com.myresume.site;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import com.myresume.admin.section.AboutSectionService;
//import com.myresume.common.entity.AboutSection;

@Controller
@ComponentScan({"com.myresume.common.entity", "com.myresume.admin.aboutsection"})
@EntityScan({"com.myresume.common.entity","com.myresume.admin.section"})
@EnableJpaRepositories("com.myresume.admin.aboutsection")
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
}
