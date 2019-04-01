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

import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.SalaService;
import com.dawes.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketEventListener {
 
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
	UsuarioService usuarioService;
    
    @Autowired
    SalaService salaService;
 
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
            
            SalaVO sala = salaService.findById(idSala).get();
            
            //si no se esta jugando
            if (!sala.isJugando()) {
            	
            	JSONObject json = new JSONObject();
            	
            	//atributo comun
                json.put("nombreUsuario", usuario.getNombre());
            	
            	//si aun hay gente en la sala
            	if (sala.getlUsuarios().size() != 0) {
                    
                    //resto de atributos del json
        			json.put("mensaje", "ha abandonado");
                    json.put("tipo", 1);
                    
                    //si el que se fue es el propietario
                    if (usuario.getIdUsuario() == sala.getIdPropietario()) {
                    	
                    	UsuarioVO nuevoPropietario = sala.getlUsuarios().get(0);
                    	
                    	sala.setIdPropietario(nuevoPropietario.getIdUsuario());
                    	salaService.save(sala);
                    	
                    	json.put("nuevoPropietarioNombre", nuevoPropietario.getNombre());
                    	json.put("nuevoPropietarioMensaje", "es el nuevo dueño de la sala");
                    }
                    
                    //hace falta poner el .toString() o el json se envia mal
                    messagingTemplate.convertAndSend("/salas/sala/" + idSala, json.toString());
                    
                    //si esta vacia la borramos
            	} else {
            		salaService.delete(sala);
            	}
            	
            	//enviamos el mensaje al chat pero no para que aparezca, si no para que el jugador se redirija a la lista de salas
            	json.put("irse", "aux"); //el valor que tenga da igual, con que no sea "" vale
                messagingTemplate.convertAndSend("/salas/sala/" + idSala + "/chat", json.toString());
            }
        }
    }
     
}