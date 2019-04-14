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
import com.dawes.modelo.MensajeVO;
import com.dawes.modelo.MensajeVO.Tipo;
import com.dawes.modelo.PartidaVO;
import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioPartidaVO;
import com.dawes.modelo.UsuarioUsuarioVO;
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
	public String enviarMensaje(@Payload(required = false) String mensaje, Authentication authentication) {
		return jsonMensaje(mensaje, authentication.getName());
	}
	
	//retorna el mensaje en forma de json con el nombre de usuario y el mensaje tratado
	public String jsonMensaje(String mensaje, String nombre) {
		
		JSONObject json = new JSONObject();
		
		//null solo seria cuando desde se manda desde la consola del navegador o se cambia el js
		if (mensaje != null) {
			json.put("nombreUsuario", nombre);
			json.put("mensaje", Utils.XSSProtection(mensaje));
		}
		
		return json.toString();
	}

	// controller para el chat de una sala en concreto
	@MessageMapping("/salas/sala/{idSala}/chat/enviarMensaje")
	@SendTo("/salas/sala/{idSala}/chat")
	public String enviarMensajeSala(@Payload(required = false) String mensaje, Authentication authentication) {
		return jsonMensaje(mensaje, authentication.getName());
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

			//hace falta buscar otra vez la sala para que tenga el usuario que se acaba de unir en ella
			json.put("sala", new ObjectMapper().writeValueAsString(salaService.findById(sala.getIdSala()).get()));
			json.put("nombreUsuario", usuario.getNombre());
			json.put("mensaje", "se ha unido");
			json.put("tipo", 0);

			mensaje = json.toString();

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			mensaje = null;
		}

		return mensaje;
	}
	
	// controller para cuando alguien se une a una sala
	// lo llama todo el mundo que se una a una sala
	@MessageMapping("/salas/sala/{idSala}/expulsarJugador")
	@SendTo("/salas/sala/{idSala}")
	public String expulsarJugador(@DestinationVariable("idSala") int idSala, @Payload(required = false) String nombreUsuarioExpulsar, StompHeaderAccessor headerAccessor,
			Authentication authentication) {

		//si el nombre del usuario a expular es null es que se llego aqui a traves de la consola del navegador
		if (nombreUsuarioExpulsar == null) return null;
		
		String mensaje;

		try {
			UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
			UsuarioVO usuExpulsar = usuarioService.findByNombre(nombreUsuarioExpulsar);
			SalaVO sala = salaService.findById(idSala).get();

			JSONObject json = new JSONObject();
			
			//en el caso de que alguien ejecute el js de expulsar un usuario comprobamos que haya sido el dueño de la sala
			if (usuario.getIdUsuario() == sala.getIdPropietario()) {
				
				json.put("nombreUsuario", usuario.getNombre());
				json.put("nombreUsuarioExpulsado", usuExpulsar.getNombre());
				json.put("mensajeExpulsar", "ha expulsado a"); //como no tiene el mismo formato que los otros mensajes se trata distinto en el js
				json.put("tipo", 2);
				
			} else {

				// mandamos un json vacio sin mas
				// en el js se recibira un {} y lo tratara sin problemas
				json = new JSONObject();
			}
			
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

		String mensaje;
		
		try
		{
			UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
			SalaVO sala = salaService.findById(idSala).get();
	
			JSONObject json = new JSONObject();
	
			//en el caso de que alguien ejecute el js de lanzar la partida comprobamos que haya sido el dueño de la sala
			if (usuario.getIdUsuario() == sala.getIdPropietario()) {
				
				//si al menos hay 2 jugadores
				if (sala.getlUsuarios().size() > 1) {
					
					// creamos la partida
					PartidaVO partida = new PartidaVO();
					partidaUtils.transformarPartida(partida);
					partidaService.save(partida);
					
					//indicamos que se esta jugando en la sala
					sala.setJugando(true);
					salaService.save(sala);
		
					//atributos del mensaje que se mandaran al js
					json.put("tipo", 3);
					json.put("idPartida", partida.getIdPartida());
					json.put("mensaje", "Ha empezado la partida");
					json.put("nombreUsuario", usuario.getNombre());
		
					// se crea y se lanza el hilo de la partida
					new Thread() {
						public void run() {
							cantarNumeros(partida.getIdPartida(), sala);
						}
					}.start();
					
				} else {
					json.put("tipo", 3);
					json.put("error", "Jugadores insuficientes para empezar la partida");
				}
	
			} else {
	
				// mandamos un json vacio sin mas
				// en el js se recibira un {} y lo tratara sin problemas
				json = new JSONObject();
			}
			
			mensaje = json.toString();
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			mensaje = null;
		}

		return mensaje;

	}

	//canta los 90 numeros de la tombola
	//lo llama en forma de hilo quien crea la partida
	public void cantarNumeros(int idPartida, SalaVO sala) {

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

				//enviamos el array al javascript
				this.template.convertAndSend("/partida/" + idPartida + "/tombola", numeros);

				//esperamos por el siguiente
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		//indicamos que acabo la partida en la sala
		sala.setJugando(false);
		salaService.save(sala);
		
		this.template.convertAndSend("/partida/" + idPartida + "/tombola", "Partida terminada");
		
		//damos un margen para que la gente vuelva a la sala (30 segundos)
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//si no se empezo otra partida y no hay nadie en la sala
		//(se busca otra vez para refrescar la lista de usuarios)
		sala = salaService.findById(sala.getIdSala()).get();
		if (!sala.isJugando() && sala.getlUsuarios().size() == 0) {
			salaService.delete(sala);
		}
	}

	//controller para el chat de una partida en concreto
	@MessageMapping("/partida/{idPartida}/chat/enviarMensaje")
	@SendTo("/partida/{idPartida}/chat")
	public String enviarMensajePartida(@Payload(required = false) String mensaje, Authentication authentication) {
		return jsonMensaje(mensaje, authentication.getName());
	}
	
	//controller para cuando alguien tacha un numero, canta linea o canta bingo
	@MessageMapping("/partida/{idPartida}/accionesCarton")
  	@SendTo("/partida/{idPartida}")
  	public String accionesCarton(@Payload String mensaje, @DestinationVariable("idPartida") int idPartida,
  			Authentication authentication, StompHeaderAccessor headerAccessor) {
  		
  		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
  		
  		//json que contiene lo que paso en el cliente
  		JSONObject json = new JSONObject(mensaje);
  		
  		//json que va contener la respuesta y atributos comunes para todos los tipos
  		JSONObject respuesta = new JSONObject();
  		respuesta.put("tipo", json.get("tipo"));
  		respuesta.put("nombreUsuario", usuario.getNombre());
  		
  		List<Integer> lNumerosTombola;
  		CartonVO carton;
  		UsuarioPartidaVO usuPar;
  		
  		switch (json.get("tipo").toString()) {
  			case "0": //se marco un numero
  				
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
  					int premio = PartidaVO.PREMIO_FICHAS_LINEA[posicion - 1];
  					
  					//sumamos las fichas al usuario y a su puntuacion total
  					usuario.setFichas(usuario.getFichas() + premio);
  					usuario.setPuntuacionTotal(usuario.getPuntuacionTotal() + premio);
  					
  					//guardamos el usuario
  					usuarioService.save(usuario);
  					
  					//texto que se pasa al cliente
  					respuesta.put("mensaje", "ha cantado linea y se ha llevado " + premio + " fichas!");
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
  					int premio = PartidaVO.PREMIO_FICHAS_BINGO[posicion - 1];
  					
  					//sumamos las fichas y la puntuacion al usuario dependiendo de su posicion
  					usuario.setFichas(usuario.getFichas() + premio);
  					usuario.setPuntuacionTotal(usuario.getPuntuacionTotal() + premio);
  					
  					//guardamos el usuario
  					usuarioService.save(usuario);
  					
  					//texto que se pasa al cliente
  					respuesta.put("mensaje", "ha cantado bingo y se ha llevado " + premio + " fichas!");
  				}
  				
  				break;
  		}

  		return respuesta.toString();
	}
	
	//comunicaciones entre usuarios para mensajes individuales
	//{nombreUsuario} es el usuario que va recibir el mensaje
	@MessageMapping("/mensajes/{nombreUsuario}/enviarMensaje")
	@SendTo("/mensajes/{nombreUsuario}")
	public String enviarMensajeUsuario(@DestinationVariable("nombreUsuario") String nombreUsuario, @Payload(required = false) String mensaje, Authentication authentication) {
		
		JSONObject jsonRespuesta = new JSONObject(jsonMensaje(mensaje, authentication.getName()));
		
		//si la respuesta no es un json vacio es que se convirtio bien a json y si el mensaje no esta vacio (que puede darse si el filtro XSS borro todo)
		if (!jsonRespuesta.isEmpty() && !jsonRespuesta.get("mensaje").equals("")) {
			
			mensaje = jsonRespuesta.getString("mensaje");
			
			//obtenemos el usuario logueado
			UsuarioVO usuarioEnviador = usuarioService.findByNombre(authentication.getName());
			UsuarioVO usuarioRecibidor = usuarioService.findByNombre(nombreUsuario); //esto ayuda pila, cambia por ejemplo de "ALEX" a como estaba originalmente el nombre, indispensable para las suscripciones en el cliente
			
			UsuarioUsuarioVO usuUsu;
			
			//obtenemos la relacion entre el usuario logueado y el usuario al que envia el mensaje
			//(recordar que la relacion puede estar en cualquiera de las dos listas)
			usuUsu = Utils.getRelacion(usuarioEnviador, usuarioRecibidor);
			
			//si existe la relacion
			if (usuUsu != null) {
				
				//si ya son amigos
				if (usuUsu.isActivo()) {
					
					//añadimos el mensaje
					usuUsu.getlMensajes().add(new MensajeVO(usuUsu, authentication.getName(), mensaje, Tipo.MENSAJE));
					
					//si hay muchos mensajes borramos el primero
					if (usuUsu.getlMensajes().size() > UsuarioUsuarioVO.CAP_HISTORIAL_MAX) {
						usuUsu.getlMensajes().remove(0);
					}
					
					//guardamos el mensaje en la base de datos
					usuarioService.save(usuarioEnviador);
					
					//notificacion que le llega al usuario este donde este
					Utils.enviarNotificacionUsuario(template, usuarioEnviador.getNombre(), mensaje, "/perfil/mensajes/ver", usuarioRecibidor.getNombre());
					
				} else {
					jsonRespuesta = new JSONObject();
				}
				
			} else {
				//devolvemos un json vacio como para los otros errores en los que no hay que hacer nada en el js cliente 
				jsonRespuesta = new JSONObject();
			}
			
		}
		
		return jsonRespuesta.toString();
	}
	
	//permite invitar un jugador a una sala
	//este metodo no devuelve ningun mensaje, si no que llama a utils para mandarselo al usuario este donde este
	@MessageMapping("/usuario/{nombreUsuario}/invitarSala")
	public void invitarUsuarioSala(@DestinationVariable("nombreUsuario") String nombreUsuario, Authentication authentication) {
		
		UsuarioVO usuario = usuarioService.findByNombre(authentication.getName());
		UsuarioVO usu = usuarioService.findByNombre(nombreUsuario);
		
		//si existe el usuario al que invitamos
		if (usu != null) {
			
			//obtenemos la relacion entre los usuarios
			UsuarioUsuarioVO usuUsu = Utils.getRelacionActiva(usuario, usu);
			
			//si existe la relacion
			if (usuUsu != null) {
				
				//la url va ser la de la sala en la que este el usuario que invita
				//la url la propiedad url del json y el contenido del mensaje en base de datos
				String url = "/salas/sala/" + usuario.getSala().getIdSala();
				
				Utils.enviarNotificacionUsuario(template, authentication.getName(), "Invitación a partida", url, usu.getNombre());
				
				usuUsu.getlMensajes().add(new MensajeVO(usuUsu, usuario.getNombre(), url, Tipo.INVITACION));
				
				usuarioService.save(usuario);
			}
		}
	}
}