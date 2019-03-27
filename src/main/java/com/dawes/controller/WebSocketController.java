package com.dawes.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.dawes.modelo.CartonVO;
import com.dawes.modelo.PartidaVO;
import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioPartidaVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.service.PartidaService;
import com.dawes.service.SalaService;
import com.dawes.service.UsuarioService;
import com.dawes.utils.PartidaUtils;
import com.dawes.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class WebSocketController {

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	SalaService salaService;

	@Autowired
	PartidaService partidaService;

	@Autowired
	PartidaUtils partidaUtils;

	// controller para el chat de la pantalla de listas salas
	@MessageMapping("/salas/chat/enviarMensaje")
	@SendTo("/salas/chat")
	public String enviarMensaje(@Payload String mensaje, Authentication authentication) {
		return authentication.getName() + ": " + mensaje;
	}

	// controller para el chat de una sala en concreto
	@MessageMapping("/salas/sala/{idSala}/chat/enviarMensaje")
	@SendTo("/salas/sala/{idSala}/chat")
	public String enviarMensajeSala(@Payload String mensaje, Authentication authentication) {
		return authentication.getName() + ": " + mensaje;
	}

	// controller para cuando alguien se une a una sala
	// lo llama todo el mundo que se una a una sala
	@MessageMapping("/salas/sala/{idSala}/newUser")
	@SendTo("/salas/sala/{idSala}")
	public String unirseSala(@DestinationVariable("idSala") int idSala, StompHeaderAccessor headerAccessor,
			Authentication authentication) {

		String mensaje;

		try {
			UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
			SalaVO sala = salaService.findById(idSala).get();
			usuario.setSala(sala);

			usuarioService.save(usuario);

			headerAccessor.getSessionAttributes().put("idSala", idSala); //este atributo se usa en el listener del socket para comprobar si un usuario abandona una sala
			headerAccessor.getSessionAttributes().put("idUsuario", usuario.getIdUsuario());
			System.out.println("Se ha entrado en la sala " + idSala);

			JSONObject json = new JSONObject();
			json.put("usuario", new ObjectMapper().writeValueAsString(usuario));

			//hace falta buscar otra vez la sala para que tenga el usuario que se acaba de unir en ella
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

	// controller para cuando se crea una partida
	// solo lo llama quien crea la partida
	@MessageMapping("/salas/sala/{idSala}/empezarPartida")
	@SendTo("/salas/sala/{idSala}")
	public String empezarPartida(@DestinationVariable("idSala") int idSala, Authentication authentication) {

		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		SalaVO sala = salaService.findById(idSala).get();

		JSONObject json;

		// en el caso de que alguien ejecute el js de lanzar la partida comprobamos que
		// haya sido
		// el dueño de la sala
		if (usuario.getIdUsuario() == sala.getIdPropietario()) {

			// creamos la partida
			PartidaVO partida = new PartidaVO();
			partidaUtils.transformarPartida(partida);
			partidaService.save(partida);

			// mandamos mensaje al js
			json = new JSONObject();
			json.put("idPartida", partida.getIdPartida());
			json.put("mensaje", "Ha empezado la partida");
			json.put("tipo", 2);

			// se crea y se lanza el hilo de la partida
			new Thread() {
				public void run() {
					cantarNumeros(partida.getIdPartida());
				}
			}.start();

		} else {

			// mandamos un json vacio sin mas
			// en el js se recibira un {} y lo tratara sin problemas
			json = new JSONObject();
		}

		return json.toString();

	}

	//canta los 90 numeros de la tombola
	//lo llama en forma de hilo quien crea la partida
	public void cantarNumeros(@Payload int idPartida) {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// se generan 90 numeros sin repetir
		Set<Integer> numeros = new LinkedHashSet<Integer>();
		
		while (numeros.size() < 90) {
			Integer next = ThreadLocalRandom.current().nextInt(1, 90 + 1);

			if (numeros.add(next)) {

				this.template.convertAndSend("/partida/" + idPartida + "/tombola", numeros);

				/*try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}

		this.template.convertAndSend("/partida/" + idPartida + "/tombola", "Partida terminada");
	}

	//controller para el chat de una partida en concreto
	@MessageMapping("/partida/{idPartida}/chat/enviarMensaje")
	@SendTo("/partida/{idPartida}/chat")
	public String enviarMensajePartida(@Payload String mensaje, Authentication authentication) {
		return authentication.getName() + ": " + mensaje;
	}
	
	//controller para cuando alguien tacha un numero, canta linea o canta bingo
	@MessageMapping("/partida/{idPartida}/accionesCarton")
  	@SendTo("/partida/{idPartida}")
  	public String accionesCarton(@Payload String mensaje, @DestinationVariable("idPartida") int idPartida,
  			Authentication authentication, StompHeaderAccessor headerAccessor) {
  		
  		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
  		
  		//json que contiene lo que paso en el cliente
  		JSONObject json = new JSONObject(mensaje);
  		
  		//json que va contener la respuesta
  		JSONObject respuesta = new JSONObject();
  		respuesta.put("tipo", json.get("tipo"));
  		
  		List<Integer> lNumerosTombola;
  		CartonVO carton;
  		UsuarioPartidaVO usuPar;
  		
  		switch (json.get("tipo").toString()) {
  			case "0": //se marco un numero
  				respuesta.put("nombreUsuario", usuario.getNombre());
  	  			respuesta.put("numerosRestantes", json.get("numerosRestantes"));
  	  			break;
  			case "1": //se hizo linea
  				
  				//numeros que han salido de la tombola
  				lNumerosTombola = Utils.convertToList(json.getJSONArray("numerosTombola"));
  				
  				//carton del usuario
  				carton = (CartonVO) headerAccessor.getSessionAttributes().get("carton");
  				
  				//tabla intermedia entre el usuario y la partida, contiene las posiciones
  				usuPar = usuario.getlUsuPar().stream().filter(x -> x.getPartida().getIdPartida() == idPartida).findFirst().get();
  				
  				//si alguna linea esta completa y no canto ya linea
  				if ((lNumerosTombola.containsAll(carton.getlNumeros().subList(0, 9).stream().filter(x -> x != null).collect(Collectors.toList()))
  						|| lNumerosTombola.containsAll(carton.getlNumeros().subList(9, 18).stream().filter(x -> x != null).collect(Collectors.toList()))
  						|| lNumerosTombola.containsAll(carton.getlNumeros().subList(18, 27).stream().filter(x -> x != null).collect(Collectors.toList())))
  						&& usuPar.getPosicionLinea() == 0) {
  					
  					//obtenemos la posicion mayor de cangar linea de uno de los jugadores de la partida
  					//cuando los insertamos al principio la posicion mayor es 0
  					int posicion = partidaService.findById(idPartida).get().getlUsuPar().stream().mapToInt(x -> x.getPosicionLinea()).max().getAsInt();
  					
  					//actualizamos la posicion del usuario en la partida sumandole 1
  					usuPar.setPosicionLinea(++posicion);
  					
  					//obtenemos las fichas para esa posicion
  					//-1 debido a que la posicion 1 (primero) refleja el indice 0 en la lista
  					int premioFichas = PartidaVO.PREMIO_FICHAS_LINEA[posicion - 1];
  					
  					//sumamos las fichas al usuario
  					usuario.setFichas(usuario.getFichas() + premioFichas);
  					
  					//guardamos el usuario
  					usuarioService.save(usuario);
  					
  					//texto que se pasa al cliente
  					respuesta.put("mensaje", usuario.getNombre() + " ha cantado linea y se ha llevado " + premioFichas + " fichas!");
  				}
  		  		
  		  		break;
  			case "2": //se hizo bingo
  				
  				//numeros que han salido de la tombola
  				lNumerosTombola = Utils.convertToList(json.getJSONArray("numerosTombola"));
  				
  				//carton del usuario
  				carton = (CartonVO) headerAccessor.getSessionAttributes().get("carton");
  				
  				//tabla intermedia entre el usuario y la partida, contiene las posiciones
  				usuPar = usuario.getlUsuPar().stream().filter(x -> x.getPartida().getIdPartida() == idPartida).findFirst().get();
  				
  				//si todos los numeros del carton estan en los numeros de la tombola y no canto bingo ya
  				if (lNumerosTombola.containsAll(carton.getlNumeros().stream().filter(x -> x != null).collect(Collectors.toList()))
  						&& usuPar.getPosicionBingo() == 0) {

  					//obtenemos la posicion de cantar bingo mayor de uno de los jugadores de la partida
  					//cuando los insertamos al principio la posicion mayor es 0
  					int posicion = partidaService.findById(idPartida).get().getlUsuPar().stream().mapToInt(x -> x.getPosicionBingo()).max().getAsInt();
  					
  					//actualizamos la posicion del usuario en la partida sumandole 1
  					usuPar.setPosicionBingo(++posicion);
  					
  					//obtenemos las fichas para esa posicion
  					//-1 debido a que la posicion 1 (primero) refleja el indice 0 en la lista
  					int premioFichas = PartidaVO.PREMIO_FICHAS_BINGO[posicion - 1];
  					
  					//sumamos las fichas al usuario dependiendo de su posicion
  					usuario.setFichas(usuario.getFichas() + premioFichas);
  					
  					//guardamos el usuario
  					usuarioService.save(usuario);
  					
  					//texto que se pasa al cliente
  					respuesta.put("nombreUsuario", usuario.getNombre()); //se usa para comprobar que cliente hizo bingo y mostrarle el enlace de volver a la sala
  					respuesta.put("mensaje", usuario.getNombre() + " ha cantado bingo y se ha llevado " + premioFichas + " fichas!");
  				}
  				
  				break;
  		}

  		return respuesta.toString();
}

}