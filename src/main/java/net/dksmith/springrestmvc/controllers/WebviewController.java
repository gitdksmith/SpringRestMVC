package net.dksmith.springrestmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.dksmith.springrestmvc.domain.Client;
import net.dksmith.springrestmvc.services.GeoIpService;

@Controller
public class WebviewController {
	
	@Autowired
	GeoIpService geoIpService;

	@Value("${spring.application.name}")
	private String appName;
	
	@GetMapping("/")
	public String indexPage(Model model) {
		model.addAttribute("appName", appName);
		return "index";
	}
	
	@GetMapping("/request-info")
	@ResponseBody
	public Client reflectRequestInfo(HttpServletRequest request, Client client) {
		client.setFromRequest(request, geoIpService);
		return client;
	}
}
