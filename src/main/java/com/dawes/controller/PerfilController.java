package com.dawes.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dawes.modelo.UsuarioVO;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping("/{nombre}")
	public String mostrar(@PathVariable("nombre") String nombre, Model model) {
		
		UsuarioVO usuario = usuarioService.findByNombre(nombre);
		
		model.addAttribute("usuario", usuario);
		
		return RR.CARPETA_PERFILES + "perfil";
	}
	
	@RequestMapping("/actualizarPerfil")
	public String actualizarPerfil(@RequestParam("imagen") MultipartFile image, Authentication authentication) {
		
		UsuarioVO usuario = usuarioService.findByNombre("Dani");
		
		if (!image.isEmpty()) {
			
			try {
				//lo maximo que deja mysql por defecto son 4MB, el maximo que dejamos en el servidor
				//tanto como de subida de archivos como de request son 3MB por si acaso
				//TODO: acordarse de tratar ambos errores en el error handler porque si no se interrumpe la conexion
				usuario.setImagenPerfil(Base64.getEncoder().encodeToString(image.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			usuario.setImagenPerfil(null);
		}
		
		usuarioService.save(usuario);
		
		return "redirect:/perfil/" + usuario.getNombre();
	}
	
	@RequestMapping("/{nombre}/imagen")
	public void getImagenAsBase64(@PathVariable("nombre") String nombre, HttpServletResponse response,HttpServletRequest request) 
	          throws ServletException, IOException{
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		
		UsuarioVO usuario = usuarioService.findByNombre(nombre);
		
		if (usuario.getImagenPerfil() != null) {
			
		    response.getOutputStream().write(Base64.getDecoder().decode(usuario.getImagenPerfil()));
		    
		} else {
			//esto probablemente no va funcionar cuando lo pasemos a war
			Resource resource = new ClassPathResource("/static/imgs/fotodefecto.jpg");
			response.getOutputStream().write(Files.readAllBytes(resource.getFile().toPath()));
		}
	    
		response.getOutputStream().close();
		
	}
}
