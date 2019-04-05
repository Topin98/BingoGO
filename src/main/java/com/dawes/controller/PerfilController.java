package com.dawes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.dawes.modelo.UsuarioUsuarioVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
	
	@Autowired
	SimpMessagingTemplate template;
	
	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping("/{nombre}")
	public String mostrar(@PathVariable("nombre") String nombre, Model model, Authentication authentication) {
		
		//usuario que se va a ver el perfil
		UsuarioVO usu = usuarioService.findByNombre(nombre);
		
		//si existe el usuario redireccionamos al perfil del que inicio sesion
		if (usu == null) return "redirect:/perfil/" + authentication.getName() + "?error=El usuario no existe";
		
		//numero de partidas jugadas
		int size = usu.getlUsuPar().size();
		
		//numero de veces que quedo primero segundo y tercero cantando bingo y linea
		long numPrimeroB = usu.getlUsuPar().stream().filter(x -> x.getPosicionBingo() == 1).count();
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
	
	@RequestMapping("/amigos/proponerAmistad")
	public String proponerAmistad(@RequestParam String nombreUsuario, Authentication authentication) {
		String resultado = "redirect:/perfil/";
		
		//si no se mando solicitud a si mismo
		//(importante el ignore case porque en la url puede editarse como se quiera)
		if (!authentication.getName().equalsIgnoreCase(nombreUsuario)) {
					
			UsuarioVO usuario = usuarioService.findByNombre(authentication.getName()); //usuario que tiene la sesion iniciada
			UsuarioVO usu = usuarioService.findByNombre(nombreUsuario); //usuario al que se va mandar la solicitud
		
			if (usu == null) return resultado + usuario.getNombre();
			
			UsuarioUsuarioVO usuUsu = new UsuarioUsuarioVO(new LinUsuUsuVO(), usuario, usu, false, new ArrayList<MensajeVO>());
			
			usuario.getlUsuUsuRequest().add(usuUsu);
			
			usuUsu.getlMensajes().add(new MensajeVO(usuUsu, usuario.getNombre(), "Solicitud de amistad!", Tipo.PETICION));
			
			try{
				usuarioService.save(usuario);
				
				//enviamos la notifiacion al usuario
				Utils.enviarNotificacionUsuario(template, usuario.getNombre(), "Solicitud de amistad!", "/perfil/mensajes", usu.getNombre());
				
				resultado += usu.getNombre();
				
			} catch (Exception e) {
				resultado += usu.getNombre() + "?error=Ya sois amigos o ya ha mandado una solicitud de amistad";
			}
			
		} else {
			resultado += authentication.getName() + "?error=No puedes ser amigo de ti mismo";
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
			
			resultado += "mensajes";
			
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
	
	@RequestMapping("/mensajes")
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
	
	
}