package com.dawes.utils;

import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dawes.modelo.LinUsuRolVO;
import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioRolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;

public class UsuarioUtils {
	
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	@Autowired
	RolService rolService;
	
	public void transformarUsuario(UsuarioVO usuario) {
		
		//añadimos el rol al usuario
		//(cuando se registran tienen el rol de usuario que tiene el id 1)
		RolVO rol = rolService.findById(1).get();
		UsuarioRolVO usuRol = new UsuarioRolVO(new LinUsuRolVO(), usuario, rol);
		usuario.getlUsuarioRol().add(usuRol);
		
		//quitamos los posibles caracteres raros del nombre
		usuario.setNombre(XSSProtectionNombre(usuario.getNombre()));
		
		//al recibirse el objeto del formulario de registro la fecha es null
		usuario.setFechaRegistro(LocalDate.now());
				
		//el usuario por defecto esta activado
		usuario.setEnabled(true);
		
		if (Utils.validarCadena(usuario.getPassword())) {
			//encriptamos la contraseña
			usuario.setPassword(pwEncoder.encode(usuario.getPassword()));
		}
	}
	
	public boolean validarUsuario(UsuarioVO usuario) {
		
		return Utils.validarCadena(usuario.getNombre()) && Utils.validarCadena(usuario.getCorreo()) &&
				Utils.validarCadena(usuario.getPassword()) && usuario.getFechaRegistro() != null;
		
	}
	
	//filtro de caracteres raros para el registro de usuario
	public static String XSSProtectionNombre(String cadena) {
			
		//elimina caracteres raros
		String aux = cadena.replaceAll("[^a-zA-Z0-9\\s]", "");
		
		//filtro xss
		return Jsoup.clean(aux, Whitelist.basic());
	}
	
}
