package com.dawes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.service.UsuarioService;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping
	public String index(Authentication authentication, Model model) {
		
		if (Utils.isLogged(authentication)) {
			model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		}
		
		return "index";
	}
}
