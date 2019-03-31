package com.dawes.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.UsuarioVO;
import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.UsuarioUtils;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/")
public class SesionController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioUtils usuarioUtils;
	
	@RequestMapping("/login")
	public String mostrarFormLogin(Authentication authentication) {
		
		//si no esta logeado
		if (!Utils.isLogged(authentication)) {
			return RR.CARPETA_SESIONES + "login";
			
			//si esta logueado lo redirigimos al index
		} else {
			return "redirect:/";
		}
		
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String mostrarFormRegistro(Model model, @RequestParam(required=false) String error, Authentication authentication) {
		
		//si no esta logeado
		if (!Utils.isLogged(authentication)) {
		    UsuarioVO usuario = new UsuarioVO();
		    
		    model.addAttribute("usuario", usuario);
		    model.addAttribute("error", error);
		    
		    return RR.CARPETA_SESIONES + "registro";
		    
		    
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	public String registrarse(Model model, UsuarioVO usuario, HttpServletRequest request) {
		
		String resultado = "redirect:";
		
		usuarioUtils.transformarUsuario(usuario);
		
		//comprobamos que el nombre y la contraseña no se hayan rellenado solo con espacios en blanco
		if (usuarioUtils.validarUsuario(usuario)) {
			
			try{
				usuarioService.save(usuario);
				autologin(usuario, request);
				resultado += "/";
				
			} catch (DataIntegrityViolationException e) {
				
				//si el error es que es una clave duplicada
				if(e.getMostSpecificCause() instanceof SQLIntegrityConstraintViolationException){
					
					resultado += "/registro?error=";
					
					//ya existe un usuario con ese nombre
					if (usuarioService.findByNombre(usuario.getNombre()) != null) {
						resultado += "Ya hay un usuario registrado con ese nombre. ";
					}

					//ya existe un usuario con ese correo
					if (usuarioService.findByCorreo(usuario.getCorreo()) != null) {
						resultado += "Ya hay un usuario registrado con ese correo.";
					}
					
					//si no es que el nombre o el correo son demasiados largo
				} else {
					resultado += "/registro?error=El usuario y/o el correo son demasiado largos";
				}
				
			}
			
			//se han metido solo espacios en el usuario o la contraseña
		} else {
			resultado += "/registro?error=El nombre de usuario y la contrase%C3%B1a no pueden estar vac%C3%ADos";
			
		}
		
		return resultado;
	}
	
	//loguea a un usuario a partir de su usuario y su contraseña
	//este metodo solo es llamado justo despues de que un usuario se registre, si se hace un login normal lo tratara el propio spring
	public void autologin(UsuarioVO usuario, HttpServletRequest request) {
		
		UserDetails userDetails = usuarioService.loadUserByUsername(usuario.getNombre());
		
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	}
}
