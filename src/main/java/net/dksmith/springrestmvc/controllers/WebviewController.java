package net.dksmith.springrestmvc.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebviewController {

	@Value("${spring.application.name}")
	private String appName;
	
	@GetMapping("/")
	public String indexPage(Model model) {
		model.addAttribute("appName", appName);
		return "index";
	}
}
