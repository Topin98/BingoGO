package com.dawes.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.SalaService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.SalaUtils;
import com.dawes.utils.Utils;

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
	public String listar(@RequestParam(required = false, defaultValue = "1") Integer pagina, //numero de pagina
			@RequestParam(required = false, defaultValue = "") String nombreSala, @RequestParam(required = false) Boolean jugando, @RequestParam(required = false) Boolean privadas, //filtros que se pueden aplicar
			Model model, Authentication authentication) {
		
		if (jugando == null) jugando = false;
		if (privadas == null) privadas = false;
		
		//(desde que pagina, tamaño por pagina, criterio de busqueda)
		//(el primer parametro hay que poner menos 1 para que coja el numero de pagina bien)
		Page<SalaVO> pSalas = salaService.getSalasFiltradas(PageRequest.of(pagina - 1, Utils.PAGESIZE, Sort.by("idSala")), nombreSala, jugando, !privadas); //privadas los invertimos porque se busca por publicas
		
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		model.addAttribute("listaSalas", pSalas);
		model.addAttribute("numPaginas", Utils.getNumPaginas(pSalas)); //obtenemos los numeros de paginas de las salas
		model.addAttribute("nuevaSala", new SalaVO()); //sala que igual crea el usuario
		
		//para actualizar el estado de los checkbox al refrescar la pagina y la paginacion
		model.addAttribute("nombrePagina", nombreSala);
		model.addAttribute("jugando", jugando);
		model.addAttribute("privadas", privadas);
		
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
	
	@RequestMapping("/salaAleatoria")
	public String salaAleatoria() {
		String resultado = "redirect:";
		
		//obtenemos una sala aleatoria que se pueda unir sin problemas
		SalaVO sala = salaService.getSalaAleatoria(SalaVO.CAP_MAX);
		
		//si hay alguna sala disponible
		if (sala != null) {
			
			//le redigimos a la sala
			resultado += "/salas/sala/" + sala.getIdSala();
			
			//si no es que no habia ninguna sala disponible
		} else {
			resultado += "/salas?salasNoDis=No hay ninguna sala disponible actualmente";
		}
		
		return resultado;
	}
	
}
