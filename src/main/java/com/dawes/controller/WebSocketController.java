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
import com.fasterxml.jackson.databind.ObjectMapper;
 
@Controller
public class WebSocketController {
 
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	SalaService salaService;
	
	//controller para el chat de la pantalla de listas salas
	@MessageMapping("/salas/chat/enviarMensaje")
    @SendTo("/salas/chat")
    public String enviarMensaje(@Payload String mensaje, Authentication authentication) {
        return authentication.getName() + ": " + mensaje;
    }
	
	//controller para el chat de una sala en concreto
	@MessageMapping("/salas/sala/{idSala}/chat/enviarMensaje")
    @SendTo("/salas/sala/{idSala}/chat")
    public String enviarMensajeSala(@Payload String mensaje, Authentication authentication) {
        return authentication.getName() + ": " + mensaje;
    }
	
	//controller para cuando alguien se une a una sala
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
            json.put("usuario", new ObjectMapper().writeValueAsString(usuario));
            
            //(esta parte es recomendable y probablemente mas optimo hacerlo con web services de forma que al clicar
            //en cada jugador haga una peticiona ajax con sus datos, si se encuentra la manera de hacer quitar la sala
            //del json)
            //hacer falta buscar otra vez la sala para que tenga el usuario que se acaba de unir en ella
            json.put("sala", new ObjectMapper().writeValueAsString(salaService.findById(sala.getIdSala()).get()));
            json.put("mensaje", usuario.getNombre() + " se ha unido");
            json.put("tipo", 0);
            
            mensaje = json.toString();
	        
        } catch (Exception e) {
        	System.out.println("Error: " + e.getMessage());
        	mensaje = null;
        }
        
        return mensaje;
    }

    @MessageMapping("/salas/sala/{idSala}/empezarPartida")
    @SendTo("/salas/sala/{idSala}")
    public String empezarPartida(@DestinationVariable("idSala") int idSala, Authentication authentication) {
    	
    	JSONObject json = new JSONObject();
        json.put("mensaje", "Ha empezado la partida");
        json.put("tipo", 2);
        
    	return json.toString();
    }
 
}