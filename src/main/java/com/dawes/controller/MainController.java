package com.dawes.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioRolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Authentication authentication) {
		
		if (authentication != null && authentication.isAuthenticated()) {
			System.out.println(authentication.getPrincipal());
			
		}
		
		return "index";
	}
	
	@RequestMapping("/prueba")
	public String prueba(HttpServletRequest request, Authentication authentication) {
		return "prueba";
	}
	
	@RequestMapping("/admin")
	public String admin(HttpServletRequest request, Authentication authentication) {
		return "admin";
	}
}
