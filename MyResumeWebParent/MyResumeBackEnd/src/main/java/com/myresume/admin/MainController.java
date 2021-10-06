package com.myresume.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("index")
	public String viewIndexPage() {
		return "redirect:/";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}
}
