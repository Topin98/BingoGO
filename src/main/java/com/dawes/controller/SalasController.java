package com.dawes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.SalaService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.SalaUtils;

@Controller
@RequestMapping("/salas")
public class SalasController {
	
	@Autowired
	SalaService salaService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	SalaUtils salaUtils;
	
	@RequestMapping
	public String listar(Model model, Authentication authentication) {
		
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		model.addAttribute("listaSalas", salaService.findByActiva(true));
		model.addAttribute("nuevaSala", new SalaVO()); //sala que igual crea el usuario
		
		return RR.CARPETA_SALAS + "listaSalas";
	}
	
	@RequestMapping("/sala/{idSala}")
	public String mostrarSala(@PathVariable("idSala") int idSala, Model model, Authentication authentication) {
		String resultado;
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		SalaVO sala = salaService.findById(idSala).get();
		
		//si no esta en ninguna sala
		if (usuario.getSala() == null) {
			
			model.addAttribute("sala", sala);
			
			resultado = RR.CARPETA_SALAS + "sala";
			
		} else {
			
			resultado = "redirect:/salas?yaEnSala=Es necesario salir de la sala actual";
		}
			
		return resultado;
	}
	
	@RequestMapping("/crearSala")
	public String crearSala(Model model, SalaVO sala, Authentication authentication) {
		String resultado = "redirect:";
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		//si no esta en ninguna sala
		if (usuario.getSala() == null) {
			
			salaUtils.transformarSala(sala, usuario);
			
			try {
				
				salaService.save(sala);
				
				resultado += "/salas/sala/" + sala.getIdSala();
				
				//si peta es porque se introdujeron mas de 30 caracteres en el nombre de la sala
			} catch (Exception e) {
				
				resultado += "/salas?error=El nombre de la sala es demasiado largo";
			}
			
		} else {
			resultado += "/salas?yaEnSala=Es necesario salir de la sala actual";
		}
		
		return resultado;
	}
	
}
