package com.dawes.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.LinUsuRolVO;
import com.dawes.modelo.PremioVO;
import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioRolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.PremioService;
import com.dawes.service.RolService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.RR;
import com.dawes.utils.Utils;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	PremioService premioService;
	
	@Autowired
    JavaMailSender emailSender;
	
	@RequestMapping("/usuarios")
	public String listarUsuarios(@RequestParam(required = false, defaultValue = "1") Integer pagina,
			Model model, Authentication authentication, HttpSession session) {
		
		//obtenemos de session el posible filtro, si no hay filtro ponemos el valor por defecto para que muestre todos los usuarios
		String nombre = session.getAttribute("nombreFiltroPerfilAdmin") != null ? session.getAttribute("nombreFiltroPerfilAdmin").toString() : "";
			
		//pagina que se va mostrar con los usuarios
		Page<UsuarioVO> pPerfiles = usuarioService.findAllByNombreContaining(PageRequest.of(pagina - 1, Utils.PAGESIZE), nombre);
		
		model.addAttribute("listaUsuarios", pPerfiles);
		model.addAttribute("numPaginas", Utils.getNumPaginas(pPerfiles)); //obtenemos el numero de paginas que tiene la paginacion
		model.addAttribute("usuario", usuarioService.findByNombre(authentication.getName()));
		model.addAttribute("nombreFiltroPerfilAdmin", nombre); //para que aparezca en el input directamente
		
		return RR.CARPETA_ADMIN + "panelAdmin";
		
	}
	
	@RequestMapping("/usuarios/filtrar")
	public String filtrarPerfiles(@RequestParam String nombre, HttpSession session) {
		
		session.setAttribute("nombreFiltroPerfilAdmin", nombre);
		
		return "redirect:/admin/usuarios";
	}
	
	@RequestMapping("/mandarCorreo")
	public String mandarCorreo(@RequestParam String correoDestino, @RequestParam String asunto, @RequestParam String mensaje) {
		
		Utils.enviarCorreo(emailSender, correoDestino, asunto, mensaje);
		
		return "redirect:/admin/usuarios?mensaje=Se ha enviado el correo";
	}
	
	@RequestMapping("/{idUsuario}/tratarAdmin")
	public String tratarAdmin(@PathVariable("idUsuario") int idUsuario, Model model) {
		
		UsuarioVO usuario = usuarioService.findById(idUsuario).get();
		
		//2 es el id del rol de admin
		Optional<UsuarioRolVO> optionalUsuRol = usuario.getlUsuarioRol().stream().filter(x -> x.getRol().getIdRol() == 2).findFirst();
		
		//ya es admin, le quitamos el rol
		if (optionalUsuRol.isPresent()) {
			usuario.getlUsuarioRol().remove(optionalUsuRol.get());
			
			//no es admin, le damos el rol
		} else {
			RolVO rol = rolService.findById(2).get();
			UsuarioRolVO usuRol = new UsuarioRolVO(new LinUsuRolVO(), usuario, rol);
			
			usuario.getlUsuarioRol().add(usuRol);
		}
		
		usuarioService.save(usuario);
		
		return "redirect:/admin/usuarios";
	}
	
	@RequestMapping("/{idUsuario}/tratarBan")
	public String tratarBan(@PathVariable("idUsuario") int idUsuario, Model model, Authentication authentication) {
		
		UsuarioVO usuario = usuarioService.findById(idUsuario).get();
		
		//si no esta baneado
		if (usuario.isEnabled()) {
			//lo baneamos
			usuario.setEnabled(false);
			
			//si no lo desbaneamos
		} else {
			
			usuario.setEnabled(true);
		}
		
		usuarioService.save(usuario);
		
		return "redirect:/admin/usuarios";
	}
	
	@RequestMapping("/mandarCorreoPremio")
	public String mandarCorreoPremio(@RequestParam int idUsuario, @RequestParam int idPremio, @RequestParam String correoDestino) {
		
		PremioVO premio = premioService.findById(idPremio).get();
		
		Utils.enviarCorreo(emailSender, correoDestino, "Actualización de estado de su premio",
				"Hemos recibido el canjeo de su premio \"" + premio.getNombre() + "\". En esta versión aún no disponemos de "
				+ "stock para enviar premios a los jugadores, se le notificará cuando le podamos enviar su premio."
				+ "\nEsperemos que siga disfrutando y jugando a BingoGO!.");
		
		return "redirect:/admin/usuarios?mensaje=Se ha enviado el correo";
	}
	
}