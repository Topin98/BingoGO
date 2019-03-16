package com.dawes.listener;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.dawes.modelo.UsuarioVO;
import com.dawes.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketEventListener {
 
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
	UsuarioService usuarioService;
 
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        //System.out.println("entra3");
    }
 
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    	System.out.println("entra4");
    	
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
 
        Integer idSala = (Integer) headerAccessor.getSessionAttributes().remove("idSala");
        
        //si el idSala no es null es que alguien se desconecto de la sala
        if (idSala != null) {
        	
        	UsuarioVO usuario = usuarioService.findById((Integer) headerAccessor.getSessionAttributes().get("idUsuario")).get();
        	usuario.setSala(null);
        	
        	usuarioService.save(usuario);
        	
            System.out.println("Se ha salido de la sala: " + idSala);
            
            JSONObject json = new JSONObject();
            
            try {
            	
				json.put("usuario", new ObjectMapper().writeValueAsString(usuario));
				
			} catch (JSONException | JsonProcessingException e) {
				System.out.println("Error " + e.getMessage());
			}
            
            json.put("mensaje", usuario.getNombre() + " ha abandonado");
            json.put("tipo", 1);
            
            //hace falta poner el .toString() o el json se envia mal
            messagingTemplate.convertAndSend("/salas/sala/" + idSala, json.toString());
        }
    }
     
}