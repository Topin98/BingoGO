package com.dawes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;

@Controller
@RequestMapping("/ranking")
public class RankingController {

	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping
	public String mostrarRanking(Model model, Authentication authentication) {
		
		//cargamos las 3 listas y las añadimos al modelo
		model.addAttribute("lPuntuaciones", usuarioService.findTop100ByOrderByPuntuacionTotalDesc());
		model.addAttribute("lVictorias", usuarioService.getTop100UsuVictorias());
		model.addAttribute("lRicos", usuarioService.findTop100ByOrderByFichasDesc());
		
		//cargamos el usuario que esta logueado
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		
		return RR.CARPETA_RANKINGS + "ranking";
	}
}
