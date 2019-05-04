package com.dawes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dawes.modelo.LinUsuUsuVO;
import com.dawes.modelo.MensajeVO;
import com.dawes.modelo.MensajeVO.Tipo;
import com.dawes.modelo.ReportVO;
import com.dawes.modelo.UsuarioPartidaVO;
import com.dawes.modelo.UsuarioUsuarioVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.UsuarioUtils;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
	
	@Autowired
	SimpMessagingTemplate template;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioUtils usuarioUtils;
	
	@RequestMapping("/{nombre}")
	public String mostrar(@PathVariable("nombre") String nombre, Model model, Authentication authentication) {
		
		//usuario que se va a ver el perfil
		UsuarioVO usu = usuarioService.findByNombre(nombre);
		
		//si no existe el usuario redireccionamos al perfil del que inicio sesion
		if (usu == null) return "redirect:/perfil/" + authentication.getName() + "?e=El usuario no existe";
		
		//numero de partidas jugadas
		int size = usu.getlUsuPar().size();
		
		long numPrimeroB = 0;
		
		//si ha jugado partidas
		if (size != 0) {
			
			//numero de veces que quedo primero segundo y tercero cantando bingo y linea
			numPrimeroB = usu.getlUsuPar().stream().filter(x -> x.getPosicionBingo() == 1).count();
			long numSegundoB = usu.getlUsuPar().stream().filter(x -> x.getPosicionBingo() == 2).count();
			long numTerceroB = usu.getlUsuPar().stream().filter(x -> x.getPosicionBingo() == 3).count();
			long numPrimeroL = usu.getlUsuPar().stream().filter(x -> x.getPosicionLinea() == 1).count();
			long numSegundoL = usu.getlUsuPar().stream().filter(x -> x.getPosicionLinea() == 2).count();
			long numTerceroL = usu.getlUsuPar().stream().filter(x -> x.getPosicionLinea() == 3).count();
			
			//formatea con dos decimales como maximo
			DecimalFormat df = new DecimalFormat("#.##");
					
			//numero de veces en porcentaje que quedo primero segundo y tercero cantando bingo y linea
			model.addAttribute("numPrimeroB", df.format((double)numPrimeroB / size * 100) + "%");
			model.addAttribute("numSegundoB", df.format((double)numSegundoB / size * 100) + "%");
			model.addAttribute("numTerceroB", df.format((double)numTerceroB / size * 100) + "%");
			model.addAttribute("numPrimeroL", df.format((double)numPrimeroL / size * 100) + "%");
			model.addAttribute("numSegundoL", df.format((double)numSegundoL / size * 100) + "%");
			model.addAttribute("numTerceroL", df.format((double)numTerceroL / size * 100) + "%");
			
			//si no ha jugador partidas
		} else {
			model.addAttribute("numPrimeroB", "0%");
			model.addAttribute("numSegundoB", "0%");
			model.addAttribute("numTerceroB", "0%");
			model.addAttribute("numPrimeroL", "0%");
			model.addAttribute("numSegundoL", "0%");
			model.addAttribute("numTerceroL", "0%");
		}
		
		model.addAttribute("partidasJugadas", size);
		model.addAttribute("victorias", numPrimeroB);
		
		//añadimos los amigos activos al modelo
		model.addAttribute("listaAmigos", Utils.getAmigos(usu, true));
		
		//añadimos los amigos pendientes al modelo
		model.addAttribute("listaAmigosPendientes", Utils.getAmigos(usu, false));
		
		model.addAttribute("usu", usu);
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		
		return RR.CARPETA_PERFILES + "perfil";
	}
	
	@RequestMapping("/{nombre}/imagen")
	public void getImagenAsBase64(@PathVariable("nombre") String nombre, HttpServletResponse response,HttpServletRequest request) 
	          throws ServletException, IOException{
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		
		UsuarioVO usuario = usuarioService.findByNombre(nombre);
		
		if (usuario.getImagenPerfil() != null) {
			
		    response.getOutputStream().write(Base64.getDecoder().decode(usuario.getImagenPerfil()));
		    
		} else {
			Resource resource = new ClassPathResource("/static/imgs/fotodefecto.jpg");
			response.getOutputStream().write(Files.readAllBytes(resource.getFile().toPath()));
		}
	    
		response.getOutputStream().close();
		
	}
	
	@RequestMapping("/editarPerfil/editar")
	public String mostrarEditarPerfil(@RequestParam(required=false) String error, @RequestParam(required=false) String mensaje, 
			Model model, Authentication authentication) {
		
		model.addAttribute("error", error);
		model.addAttribute("mensaje", mensaje);
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		
		return RR.CARPETA_PERFILES + "editarPerfil";
	}
	
	@RequestMapping("/accion/actualizarImagen")
	public String actualizarImagen(@RequestParam("imagen") MultipartFile image, Authentication authentication, Model model) {
		String resultado = "redirect:/perfil/editarPerfil/editar?";
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		if (!image.isEmpty()) {
			
			try {
				//lo maximo que deja mysql por defecto son 4MB, el maximo que dejamos en el servidor
				//tanto como de subida de archivos como de request son 3MB por si acaso
				usuario.setImagenPerfil(Base64.getEncoder().encodeToString(image.getBytes()));
				
				resultado += "mensaje=Se han guardado los cambios";
			} catch (IOException e) {
				e.printStackTrace();
				
				resultado += "error=No se han podido guardar los cambios";
			}
			
		} else {
			usuario.setImagenPerfil(null);
			resultado += "mensaje=Se han guardado los cambios";
		}
		
		usuarioService.save(usuario);
		
		return resultado;
	}
	
	@RequestMapping("/accion/actualizarPerfil")
	public String actualizarPerfil(UsuarioVO usu, Authentication authentication, Model model, HttpServletRequest request) {
		String resultado = "redirect:/perfil/editarPerfil/editar?";
		
		UsuarioVO usuario= usuarioService.findByNombre(authentication.getName());
		
		if (usu.getPassword().length() > 5) {
			
			String antiguoNombre = usuario.getNombre();
			
			usuario.setNombre(usuarioUtils.XSSProtectionNombre(usu.getNombre()));
			usuario.setCorreo(usu.getCorreo());
			usuario.setPassword(usuarioUtils.encriptarPw(usu.getPassword()));
			
			if (usuarioUtils.validarUsuario(usuario)) {
				
				try{
					usuarioService.save(usuario);
					usuarioUtils.autologin(usuario, request);

					//si el usuario cambio el nombre
					if (!antiguoNombre.equals(usuario.getNombre())) {
					
						//xD
						//obtenemos todas las relaciones del usuario
						Utils.getRelaciones(usuario).stream()
							.map(x -> x.getlMensajes()) //mapeamos para obtener los mensajes de cada relacion (esto devuelve List<List<MensajeVO>>)
							.flatMap(List::stream) //mapeamos la lista de listas de MensajeVO a una sola lista
							.filter(x -> x.getSender().equals(antiguoNombre)) //filtramos por el antiguo nombre de usuario
							.forEach(x -> x.setSender(usuario.getNombre())); //le ponemos el nuevo nombre
							
						//guardamos el usuario
						usuarioService.save(usuario);
					}
					
					resultado += "mensaje=Se han guardado los cambios";
					
				} catch (DataIntegrityViolationException e) {
					
					//si el error es que es una clave duplicada
					if(e.getMostSpecificCause() instanceof SQLIntegrityConstraintViolationException){
						
						resultado += "error=El nombre de usuario o correo ya han sido registrados";
						
						//si no es que el nombre o el correo son demasiados largo
					} else {
						resultado += "error=El usuario y/o el correo son demasiado largos";
					}
					
				}
				
				//nombre o contraseña rellenados solo con espacios en blanco
			} else {
				resultado += "mensaje=El nombre de usuario y la contrase%C3%B1a no pueden estar vac%C3%ADos";
			}
			
			//contraseña demasiado corta
		} else {
			resultado += "mensaje=La contrase%C3%B1a es demasiado corta";
			
		}
		
		return resultado;
	}
	
	@RequestMapping("/amigos/proponerAmistad")
	public String proponerAmistad(@RequestParam String nombreUsuario, Authentication authentication) {
		String resultado = "redirect:/perfil/";
		
		//si no se mando solicitud a si mismo
		//(importante el ignore case porque en la url puede editarse como se quiera)
		if (!authentication.getName().equalsIgnoreCase(nombreUsuario)) {
					
			UsuarioVO usuario = usuarioService.findByNombre(authentication.getName()); //usuario que tiene la sesion iniciada
			UsuarioVO usu = usuarioService.findByNombre(nombreUsuario); //usuario al que se va mandar la solicitud
		
			if (usu == null) return resultado + usuario.getNombre();
			
			//si no se mando ya la peticion
			if (Utils.getRelacion(usuario, usu) == null) {
				
				UsuarioUsuarioVO usuUsu = new UsuarioUsuarioVO(new LinUsuUsuVO(), usuario, usu, false, new ArrayList<MensajeVO>());
				
				usuario.getlUsuUsuRequest().add(usuUsu);
				
				usuUsu.getlMensajes().add(new MensajeVO(usuUsu, usuario.getNombre(), "Solicitud de amistad!", Tipo.PETICION));
				
				try{
					usuarioService.save(usuario);
					
					//enviamos la notifiacion al usuario
					Utils.enviarNotificacionUsuario(template, usuario.getNombre(), "Solicitud de amistad!", "/perfil/mensajes/ver", usu.getNombre());
					
					resultado += usu.getNombre();
					
				} catch (Exception e) {
					resultado += usu.getNombre() + "?e=Ya sois amigos o ya ha mandado una solicitud de amistad";
				}
				
				//si se mando la peticion
			} else {
				resultado += authentication.getName() + "?e=Ya le has mandado una petici%C3%B3n a este usuario";
			}
			
		} else {
			resultado += authentication.getName() + "?e=No puedes ser amigo de ti mismo";
		}
		 
		return resultado;
	}
	
	@RequestMapping("/amigos/contestarAmistad")
	public String contestarAmistad(@RequestParam String nombreUsuario, @RequestParam boolean aceptar, Model model, Authentication authentication) {
		String resultado = "redirect:/perfil/";
		
		//usuario logueado
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		//usuario que mando la solicitud
		UsuarioVO usu = usuarioService.findByNombre(nombreUsuario);
		
		if (usu == null) return resultado + usuario.getNombre();
		
		//obtenemos la relacion entre los usuarios
		UsuarioUsuarioVO usuUsu = Utils.getRelacionPeticion(usuario, usu);
		
		//si existe la relacion
		if (usuUsu != null) {
			
			//si se acepto la solicitud
			if (aceptar) {
				
				//marcamos la relacion como aceptada
				usuUsu.setActivo(true);
				
				//borramos el mensaje que siempre va estar en primer lugar
				usuUsu.getlMensajes().remove(0);
				
				//añadimos un mensaje indicando que se acepto
				usuUsu.getlMensajes().add(new MensajeVO(usuUsu, usuario.getNombre(), "Solicitud aceptada", Tipo.MENSAJE));
				
				//si se rechazo
			} else {
				//eliminamos la relacion
				//(es necesario elimarla de los dos usuarios)
				usuario.getlUsuUsuReceived().remove(usuUsu);
				usu.getlUsuUsuRequest().remove(usuUsu);
			}
			
			//guardamos los cambios
			usuarioService.save(usuario);
			
			resultado += "mensajes/ver";
			
			//si no existe la relacion
		} else {
			
			resultado += usuario.getNombre();
		}
		
		return resultado;
	}
	
	@RequestMapping("/amigos/eliminarAmistad")
	public String eliminarAmistad(@RequestParam String nombreUsuario, Model model, Authentication authentication) {
		String resultado = "redirect:/perfil/";
		
		//usuario logueado
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		//usuario que se va a borrar de amigo
		UsuarioVO usu = usuarioService.findByNombre(nombreUsuario);
		
		if (usu == null) return resultado + usuario.getNombre();
			
		//obtenemos la relacion entre los usuarios
		UsuarioUsuarioVO usuUsu = Utils.getRelacion(usuario, usu);
		
		//si existe la relacion
		if (usuUsu != null) {
			
			usuUsu.getUsuario1().getlUsuUsuRequest().remove(usuUsu);
			usuUsu.getUsuario2().getlUsuUsuReceived().remove(usuUsu);
				
			//guardamos los cambios
			usuarioService.save(usuario);
			
			resultado += usu.getNombre();
			
		} else {
			resultado += usuario.getNombre();
		}
		
		return resultado;
		
	}
	
	@RequestMapping("/mensajes/ver")
	public String mostrarMensajes(Model model, Authentication authentication) {
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		model.addAttribute("listaRelaciones", Utils.getRelaciones(usuario));
		model.addAttribute("usuario", usuario);
		
		return RR.CARPETA_PERFILES + "mensajes";
		
	}
	
	@RequestMapping("/listar/perfiles")
	public String listarPerfiles(@RequestParam(required = false, defaultValue = "1") Integer pagina,
			Model model, Authentication authentication, HttpSession session) {
		
		//obtenemos de session el posible filtro, si no hay filtro ponemos el valor por defecto para que muestre todos los usuarios
		String nombre = session.getAttribute("nombreFiltroPerfil") != null ? session.getAttribute("nombreFiltroPerfil").toString() : "";
			
		//pagina que se va mostrar con los usuarios
		Page<UsuarioVO> pPerfiles = usuarioService.findAllByNombreContaining(PageRequest.of(pagina - 1, Utils.PAGESIZE), nombre);
		
		model.addAttribute("listaUsuarios", pPerfiles);
		model.addAttribute("numPaginas", Utils.getNumPaginas(pPerfiles)); //obtenemos el numero de paginas que tiene la paginacion
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		model.addAttribute("nombreFiltroPerfil", nombre); //para que aparezca en el input directamente
		
		return RR.CARPETA_PERFILES + "listaPerfiles";
		
	}
	
	@RequestMapping("/listar/filtrar")
	public String filtrarPerfiles(@RequestParam String nombre, HttpSession session) {
		
		session.setAttribute("nombreFiltroPerfil", nombre);
		
		return "redirect:/perfil/listar/perfiles";
	}
	
	@RequestMapping("/accion/reportar")
	public String reportarJugador(@RequestParam String nombreUsu, @RequestParam String motivo, Model model, Authentication authentication) {
		String resultado = "redirect:/perfil/";
		
		//usuario que va a emitir el report
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		
		//usuario que se va a reportar
		UsuarioVO usu = usuarioService.findByNombre(nombreUsu);
		
		//si el usuario al se reporta existe
		if (usu != null) {
			usuario.getlReportsEmitidos().add(new ReportVO(usuario, usu, motivo));
			
			usuarioService.save(usuario);
			
			resultado += usu.getNombre() + "?exito=Se ha enviado el report";
		} else {
			resultado += usuario.getNombre();
		}
		
		return resultado;
		
	}
	
	@RequestMapping("/{nombre}/historial")
	public String mostrarHistorialPartidas(@PathVariable("nombre") String nombre, Model model, Authentication authentication) {
		
		UsuarioVO usu = usuarioService.findByNombre(nombre);
		
		//si no existe el usuario redireccionamos al perfil del que inicio sesion
		if (usu == null) return "redirect:/perfil/" + authentication.getName() + "?e=El usuario no existe";
		
		//ordenamos las partidas del usuario por fecha mas reciente
		Collections.sort(usu.getlUsuPar(), (usuPar1, usuPar2) -> usuPar2.getPartida().getFecha().compareTo(usuPar1.getPartida().getFecha()));

		model.addAttribute("usu", usu);
		model.addAttribute("listaPartidas", usu.getlUsuPar().stream().limit(10).collect(Collectors.toList())); //obtenemos una lista de maximo 10 ultimas partidas del usuario
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		
		return RR.CARPETA_PERFILES + "historialPartidas";
	}
	
}