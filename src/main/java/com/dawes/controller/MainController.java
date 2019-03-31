package com.dawes.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@RequestMapping
	public String index(Authentication authentication, Model model) {
		
		if (Utils.isLogged(authentication)) {
			System.out.println(authentication.getPrincipal());
			model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		}
		
		return "index";
	}
	
	@RequestMapping("/admin")
	public String admin(HttpServletRequest request, Authentication authentication) {
		return "admin";
	}
}
