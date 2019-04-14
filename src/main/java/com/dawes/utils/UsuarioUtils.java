package com.dawes.utils;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.dawes.modelo.LinUsuRolVO;
import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioRolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;

public class UsuarioUtils {
	
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	UsuarioService usuarioService;
	
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
			usuario.setPassword(encriptarPw(usuario.getPassword()));
		}
	}
	
	public String encriptarPw(String plainPw) {
		return pwEncoder.encode(plainPw);
	}
	
	public boolean validarUsuario(UsuarioVO usuario) {
		
		return Utils.validarCadena(usuario.getNombre()) && Utils.validarCadena(usuario.getCorreo()) &&
				Utils.validarCadena(usuario.getPassword()) && usuario.getFechaRegistro() != null;
		
	}
	
	//loguea a un usuario a partir de su usuario y su contraseña
	//este metodo solo es llamado justo despues de que un usuario se registre o edite algo de su perfil, si se hace un login normal lo tratara el propio spring
	public void autologin(UsuarioVO usuario, HttpServletRequest request) {
		
		UserDetails userDetails = usuarioService.loadUserByUsername(usuario.getNombre());
		
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	}
	
	//filtro de caracteres raros para el registro de usuario
	public String XSSProtectionNombre(String cadena) {
			
		//elimina caracteres raros
		String aux = cadena.replaceAll("[^a-zA-Z0-9\\s]", "");
		
		//filtro xss
		return Jsoup.clean(aux, Whitelist.basic());
	}
	
}
