package com.dawes.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dawes.modelo.CartonVO;
import com.dawes.modelo.LinUsuParVO;
import com.dawes.modelo.PartidaVO;
import com.dawes.modelo.UsuarioPartidaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.PartidaService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.PartidaUtils;
import com.dawes.utils.RR;
import com.dawes.utils.UsuarioUtils;

@Controller
@RequestMapping("/partida")
public class PartidaController {
	
	@Autowired
	PartidaService partidaService;
	
	@Autowired
	PartidaUtils partidaUtils;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioUtils usuarioUtils;
	
	@RequestMapping("/{idPartida}/guardarPartidaSession")
	public String guardarPartidaSession(Model model, @PathVariable("idPartida") int idPartida,
			Authentication authentication, HttpSession session, RedirectAttributes rAttributes) {
		
		//indicamos que el jugador estuvo en la partida
		//por defecto ponemos como 0 la posicion de linea y bingo por si abandona
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		PartidaVO partida = partidaService.findById(idPartida).get();
		UsuarioPartidaVO usuPar = new UsuarioPartidaVO(new LinUsuParVO(), usuario, partida, 0, 0);
		
		usuario.getlUsuPar().add(usuPar);
		usuarioService.save(usuario);
		
		//con el id de la partida en session operaremos con el posteriormente en temas de seguridad y demas
		session.setAttribute("idPartida", partida.getIdPartida());
		
		rAttributes.addFlashAttribute("partida", partida);
		
		return "redirect:/partida/elegirCarton";
		
	}
	
	@RequestMapping("/elegirCarton")
	public String elegirCarton(@ModelAttribute("partida") PartidaVO partida, @RequestParam(required=false) String error, Model model, HttpSession session) {
		String resultado;
		
		/* Esto seria si quisieramos mostrar 5 cartones directamente y que el usuario elija uno */
		//creamos la lista de cartones disponibles (los 4 no premium)
		//List<CartonVO> lCartones = IntStream.range(0, 4).mapToObj(x -> new CartonVO()).collect(Collectors.toList());
		
		//añadimos un carton premium
		//lCartones.add(new CartonVO(true));
				
		//añadimos la lista y el id de la partida al modelo
		//model.addAttribute("listaCartones", lCartones);
		
		//si la partida es valida es que viene de una sala, todo bien
		if (partidaUtils.validarPartida(partida) || error != null) {
			model.addAttribute("idPartida", session.getAttribute("idPartida"));
			model.addAttribute("error", error);
			
			resultado = RR.CARPETA_PARTIDAS + "elegirCarton";
			
			//si no es que entro directamente o le dio para atras en la partida
		} else {
			resultado = "redirect:/salas?partida=La partida ha expirado";
			
		}
		
		return resultado;
	}
	
	//aqui igual cogerlo en plan de un formulario seria mejor (el carton)
	@RequestMapping("/{idPartida}")
	public String tratarCartonElegido(@RequestParam boolean tipo, Authentication authentication, RedirectAttributes rAttributes) {
		String resultado = "redirect:";
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		try {
			usuario.setCarton(new CartonVO(tipo));
			
			//pasa el usuario a otro controlador junto con su carton
			rAttributes.addFlashAttribute("usuario", usuario);
			
			resultado += "/partida/jugar"; 
					
			//excepcion por fichas insuficientes
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
			resultado += "/partida/elegirCarton?error=No tienes suficientes fichas para comprar un cart%C3%B3n premium";
		}
		
		return resultado;
		
	}
	
	 @RequestMapping("/jugar")
	 public String mostrarPartida(@ModelAttribute("usuario") UsuarioVO usuario, Model model, HttpSession session) {
		 String resultado;
		 
		 //si acabamos de redirigirle a la partida
		 if (usuarioUtils.validarUsuario(usuario)) {
			 
			 //metemos el carton en session para que luego el Interceptor lo pase a la "session" del socket y
			 //se pueda comprobar que nadie hace trampas
			 session.setAttribute("carton", usuario.getCarton());
			 
			 model.addAttribute("usuario", usuario);
			 model.addAttribute("partida", partidaService.findById((int)session.getAttribute("idPartida")).get());
			 model.addAttribute("idSala", session.getAttribute("idSala")); //se usa para volver a la sala al terminar la partida
			 
			 resultado = RR.CARPETA_PARTIDAS + "partida";
			 
			 //si no es que hizo refresco o lo abrio en una pestaña nueva
		 } else {
			 resultado = "redirect:/salas?partida=La partida ha expirado";
		 }
		 
		 return resultado;
	 }

}
