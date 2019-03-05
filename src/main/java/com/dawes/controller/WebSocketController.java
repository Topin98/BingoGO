package com.dawes.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.SalaService;
import com.dawes.service.UsuarioService;
 
@Controller
public class WebSocketController {
 
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	SalaService salaService;
	
	@MessageMapping("/salas/chat/enviarMensaje")
    @SendTo("/salas/chat")
    public String addUser(@Payload String mensaje, Authentication authentication) {
        return authentication.getName() + ": " + mensaje;
    }
	
    @MessageMapping("/salas/sala/{idSala}/newUser")
    @SendTo("/salas/sala/{idSala}")
    public String unirseSala(@DestinationVariable("idSala") int idSala, 
    		StompHeaderAccessor headerAccessor, Authentication authentication) {
    	
    	String mensaje;
    	
        try {
        	UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
	        SalaVO sala = salaService.findById(idSala).get();
	        usuario.setSala(sala);
	        
	        usuarioService.save(usuario);
	        
	        headerAccessor.getSessionAttributes().put("idSala", idSala);
	        headerAccessor.getSessionAttributes().put("idUsuario", usuario.getIdUsuario());
	        System.out.println("Se ha entrado en la sala " + idSala);
	        
	        JSONObject json = new JSONObject();
            json.put("nombreUsuario", usuario.getNombre());
            json.put("mensaje", usuario.getNombre() + " se ha unido");
            json.put("tipo", 0);
            
            mensaje = json.toString();
	        
        } catch (Exception e) {
        	System.out.println("error brutal" + e.getMessage());
        	mensaje = null;
        }
        
        return mensaje;
    }

    
 
}