package com.dawes.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	@RequestMapping
	public String listar(Model model, Authentication authentication) {
		
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		model.addAttribute("listaSalas", salaService.findAll());
		model.addAttribute("nuevaSala", new SalaVO()); //sala que igual crea el usuario
		
		return RR.CARPETA_SALAS + "listaSalas";
	}
	
	@RequestMapping("/sala/{idSala}")
	public String mostrarSala(@PathVariable("idSala") int idSala, Model model,
			Authentication authentication, HttpSession session) {
		String resultado;
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		Optional<SalaVO> optional = salaService.findById(idSala);
		
		//si la sala existe
		if (optional.isPresent()) {
			
			SalaVO sala = optional.get();
			
			//si no esta en ninguna sala
			if (usuario.getSala() == null) {
				
				//si no hay partida en curso
				if (!sala.isJugando()) {
				
					//si la sala no tiene contraseña o en sesion esta la contraseña correcta
					if (sala.getPassword() == null || (session.getAttribute("pw") != null && pwEncoder.matches(session.getAttribute("pw").toString(), salaService.findById(idSala).get().getPassword()))) {
						
						//si la sala no esta completa
						if (sala.getlUsuarios().size() < SalaVO.CAP_MAX) {
							
							model.addAttribute("usuario", usuario);
							model.addAttribute("sala", sala);
							
							//este atributo NO es el que se usa para comprobar si se abandona o no una sala
							//es el que se usa para volver a la sala al terminar la partida
							session.setAttribute("idSala", idSala);
							
							resultado = RR.CARPETA_SALAS + "sala";
							
							//la sala esta llena
						} else {
							resultado = "redirect:/salas?salaLlena=La sala est%C3%A1 llena";
						}
						
					} else {
						
						resultado = "redirect:/salas?pw=La contrase%C3%B1a no es correcta";
					}
				
				} else {
					
					resultado = "redirect:/salas?partidaEnCurso=No se puede unir a la sala mientras hay una partida en curso.";
				}
				
			} else {
				
				resultado = "redirect:/salas?yaEnSala=Es necesario salir de la sala actual";
			}
			
			//si se borro entre que se cargo y se intento unirse a ella 
		} else {
			resultado = "redirect:/salas?yaEnSala=La sala no existe";
		}
			
		return resultado;
	}
	
	@RequestMapping("/comprobarContraseña")
	public String comprobarContraseña(String pw, int idSala, HttpSession session) {
		
		session.setAttribute("pw", pw);
		
		return "redirect:/salas/sala/" + idSala;
	}
	
	@RequestMapping("/crearSala")
	public String crearSala(SalaVO sala, Authentication authentication, HttpSession session) {
		String resultado = "redirect:";
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		//si no esta en ninguna sala
		if (usuario.getSala() == null) {
			
			//en la sesion iria la contraseña tal cual la recibimos del formulario
			session.setAttribute("pw", sala.getPassword());
			
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
