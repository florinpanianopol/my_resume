package com.myresume.site;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ComponentScan({"com.myresume.common.entity","com.myresume.site"})
@EntityScan({"com.myresume.common.entity","com.myresume.site"})
@EnableJpaRepositories({"com.myresume.site"})
@SpringBootApplication
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
}
