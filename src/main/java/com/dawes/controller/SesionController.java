package com.dawes.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.UsuarioUtils;

@Controller
@RequestMapping("/")
public class SesionController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioUtils usuarioUtils;
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String mostrarFormRegistro(Model model) {
	    UsuarioVO usuario = new UsuarioVO();
	    
	    model.addAttribute("usuario", usuario);
	    return "registro";
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	public String registrarse(Model model, UsuarioVO usuario) {
		
		usuarioUtils.transformarUsuario(usuario);
		
		if (usuarioUtils.validarUsuario(usuario)) {
			
			usuarioService.save(usuario);
			System.out.println("bien");
		} else {
			System.out.println("mal");
		}
		
		
		return "index";
	}
}
