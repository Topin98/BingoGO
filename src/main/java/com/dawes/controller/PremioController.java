package com.dawes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.PremioVO;
import com.dawes.modelo.UsuarioPremioVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.PremioService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/premios")
public class PremioController {
	
	@Autowired
	PremioService premioService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
    JavaMailSender emailSender;
	
	@RequestMapping
	public String listar(@RequestParam(required=false) String error, @RequestParam(required=false) String mensaje, Model model, Authentication authentication) {
		
		model.addAttribute("error", error);
		model.addAttribute("mensaje", mensaje);
		model.addAttribute("listaPremios", premioService.findAll());
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		
		return RR.CARPETA_PREMIOS + "listaPremios";
	}
	
	@RequestMapping("/{nombre}/imagen")
	public void getImagenAsBase64(@PathVariable("nombre") String nombre, HttpServletResponse response,HttpServletRequest request) 
	          throws ServletException, IOException{
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		
		PremioVO premio = premioService.findByNombre(nombre);
		
		if (premio.getImagen() != null) {
			
		    response.getOutputStream().write(Base64.getDecoder().decode(premio.getImagen()));
		    
		} else {
			Resource resource = new ClassPathResource("/static/imgs/premiodefecto.png");
			response.getOutputStream().write(Files.readAllBytes(resource.getFile().toPath()));
		}
	    
		response.getOutputStream().close();
		
	}
	
	@RequestMapping("/{nombre}/canjear")
	public String canjearPremio(@PathVariable("nombre") String nombre, Authentication authentication) {
		String resultado = "redirect:/premios?";
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		PremioVO premio = premioService.findByNombre(nombre);
		
		//tiene fichas suficientes
		if (usuario.getFichas() >= premio.getPrecio()) {
			
			//indicamos que canjeo el premio
			usuario.getlUsuPre().add(new UsuarioPremioVO(usuario, premio, LocalDate.now(), false));
			
			//le descontamos las fichas
			usuario.setFichas(usuario.getFichas() - premio.getPrecio());
			
			//guardamos el usuario
			usuarioService.save(usuario);
			
			Utils.enviarCorreo(emailSender, usuario.getCorreo(), "Seguimiento premio " + premio.getNombre(),
					"El premio \"" + premio.getNombre() + "\" acaba de ser canjeado en su cuenta por un valor de " + premio.getPrecio() + " fichas. " + 
					"En breves le llegará otro correo con un enlace para realizar su seguimiento. Gracias por jugar a BingoGO!.");
			
			resultado += "mensaje";
			
			//fichas insuficientes
		} else {
			resultado += "error";
		}
		
		return resultado;
		
	}
}
