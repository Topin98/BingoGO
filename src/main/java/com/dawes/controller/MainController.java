package com.dawes.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
	
	@RequestMapping
	public String index(HttpServletRequest request, Authentication authentication) {
		
		if (authentication != null && authentication.isAuthenticated()) {
			System.out.println(authentication.getPrincipal());
		}
		
		return "home";
	}
	
	@RequestMapping("/prueba")
	public String prueba(HttpServletRequest request, Authentication authentication) {
		return "prueba";
	}
}
