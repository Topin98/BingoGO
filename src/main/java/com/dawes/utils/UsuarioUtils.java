package com.dawes.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;

public class UsuarioUtils {
	
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	@Autowired
	RolService rolService;
	
	public void transformarUsuario(UsuarioVO usuario) {
		//al recibirse el objeto del formulario de registro la fecha es null
		usuario.setFechaRegistro(LocalDate.now());
		
		//cuando se registran tienen el rol de usuario
		usuario.setRol(rolService.findById(1).get());
		
		if (Utils.validarCadena(usuario.getPassword())) {
			//encriptamos la contraseña
			usuario.setPassword(pwEncoder.encode(usuario.getPassword()));
		}
	}
	
	public boolean validarUsuario(UsuarioVO usuario) {
		
		return Utils.validarCadena(usuario.getNombre()) && Utils.validarCadena(usuario.getCorreo()) &&
				Utils.validarCadena(usuario.getPassword()) && usuario.getFechaRegistro() != null &&
				usuario.getRol() != null;
		
		
	}
	
}
