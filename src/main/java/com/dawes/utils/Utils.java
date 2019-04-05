package com.dawes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;

import com.dawes.modelo.UsuarioUsuarioVO;
import com.dawes.modelo.UsuarioVO;

public class Utils {
	
	//numero de salas por pagina que se van a mostrar
	public static final int PAGESIZE = 15;

	//comprueba que una cadena no sea null ni esta vacia
	public static boolean validarCadena(String cadena) {
		return cadena != null && !cadena.trim().isEmpty();
	}

	//devuelve una lista desde el 1 y hasta el numero total de paginas que tiene el objeto Page que se le pasa como paremtro
	//si por ejemplo tiene 5 paginas devolveria una lista de enteros [1,2,3,4,5]
	//si el objeto no tiene paginas devuelve una lista vacia
	public static <T> List<Integer> getNumPaginas(Page<T> page) {
				
		List<Integer> pageNumbers = new ArrayList<Integer>();
		
		int totalPages = page.getTotalPages();
        if (totalPages > 0) {
        	
        	//genera un array de numeros entre 1 y el numero de paginas
            pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        }
        
        return pageNumbers;
	}
		
	//convierte un JSONArray de Integers a una List de Integers
	public static List<Integer> convertToList(JSONArray array) {
		List<Integer> lista = new ArrayList<Integer>();
		
		array.forEach(x -> lista.add((int) x));
		
		return lista;
	}
	
	//comprueba si hay un usuario con la sesion iniciada
	public static boolean isLogged(Authentication authentication) {
		return authentication != null && authentication.isAuthenticated();
	}
	
	//filtro de caracteres raros para el registro de usuario
	public static String XSSProtection(String cadena) {
			
		//elimina caracteres raros
		//aqui se permiten muchos mas que en el registro, como ?,# etc
		String aux = cadena.replaceAll("<|>|\"|'|`","");
		
		//filtro xss
		return Jsoup.clean(aux, Whitelist.basic());
	}
	
	//obtenemos una lista de los usuarios con los que tiene un relacion activo o no
	public static List<UsuarioVO> getAmigos(UsuarioVO usuario, boolean activo){
		
		//filtramos de cada lista de amigos (solicitudes enviadas y recibidas) las relaciones que esten activas y mapeamos los usuarios que no somos nosotros
		List<UsuarioVO> lAmigos1 = usuario.getlUsuUsuRequest().stream().filter(x -> x.isActivo() == activo).map(UsuarioUsuarioVO::getUsuario2).collect(Collectors.toList());
		List<UsuarioVO> lAmigos2 = usuario.getlUsuUsuReceived().stream().filter(x -> x.isActivo() == activo).map(UsuarioUsuarioVO::getUsuario1).collect(Collectors.toList());
		
		//juntamos ambas listas
		lAmigos1.addAll(lAmigos2);
		
		//retornamos la lista
		return lAmigos1;
		
	}
	
	//obtenemos una lista de las relaciones del usuario con otros
	public static List<UsuarioUsuarioVO> getRelaciones(UsuarioVO usuario){
		
		//juntamos ambas listas
		List<UsuarioUsuarioVO> aux = usuario.getlUsuUsuRequest();
		aux.addAll(usuario.getlUsuUsuReceived());
		
		//retornamos la lista
		return aux;
	}
	
	//obtiene la relacion que hay entre dos usuarios si existe, si no existe devuelve null
	public static UsuarioUsuarioVO getRelacion(UsuarioVO usuario, UsuarioVO usu) {
		
		try{
			//obtenemos la relacion entre el usuario logueado y el usuario al que envia el mensaje
			//(recordar que la relacion puede estar en cualquiera de las dos listas)
			return Utils.getRelaciones(usuario).stream().filter(x -> x.getUsuario1().getNombre().equals(usuario.getNombre()) && x.getUsuario2().getNombre().equals(usu.getNombre()) ||
				x.getUsuario1().getNombre().equals(usu.getNombre()) && x.getUsuario2().getNombre().equals(usuario.getNombre())).findFirst().get();
			
			//se mando desde el navegador un mensaje a alguien que no es amigo
		} catch (Exception e) {
			
			//mostramos el error y devolvemos null 
			System.out.println("Error:" + e.getMessage());
			
			return null;
		}
	}
	
	//obtiene la relacion que hay entre dos usuarios si existe, si no existe devuelve null
	//aqui no comprueba x<->y solo x->y ya que si no el usuario que realiza la peticion podria aceptarla el mismo
	public static UsuarioUsuarioVO getRelacionPeticion(UsuarioVO usuario, UsuarioVO usu) {
		
		//usuario es el usuario que recibe la peticion (usuario2 en la relacion)
		//usu es el usuario que la mando (usuario1 en la relacion)
		
		try{
			//obtenemos la relacion entre el usuario logueado y el usuario al que envia el mensaje
			//(recordar que la relacion puede estar en cualquiera de las dos listas)
			return Utils.getRelaciones(usuario).stream().filter(x -> x.getUsuario1().getNombre().equals(usu.getNombre()) && x.getUsuario2().getNombre().equals(usuario.getNombre())).findFirst().get();
			
			//se mando desde el navegador un mensaje a alguien que no es amigo
		} catch (Exception e) {
			
			//mostramos el error y devolvemos null 
			System.out.println("Error:" + e.getMessage());
			
			return null;
		}
	}
	
	//obtiene la relacion activa que hay entre dos usuarios si existe, si no existe devuelve null
	public static UsuarioUsuarioVO getRelacionActiva(UsuarioVO usuario, UsuarioVO usu) {
		
		try{
			//obtenemos la relacion entre el usuario logueado y el usuario al que envia el mensaje
			//(recordar que la relacion puede estar en cualquiera de las dos listas)
			return Utils.getRelaciones(usuario).stream().filter(x -> x.getUsuario1().getNombre().equals(usuario.getNombre()) && x.getUsuario2().getNombre().equals(usu.getNombre()) && x.isActivo() ||
				x.getUsuario1().getNombre().equals(usu.getNombre()) && x.getUsuario2().getNombre().equals(usuario.getNombre()) && x.isActivo()).findFirst().get();
			
			//se mando desde el navegador un mensaje a alguien que no es amigo
		} catch (Exception e) {
			
			//mostramos el error y devolvemos null 
			System.out.println("Error:" + e.getMessage());
			
			return null;
		}
	}
	
	public static void enviarNotificacionUsuario(SimpMessagingTemplate template, String nombreEnviador, String mensaje, String url, String nombreRecibidor) {
		
		JSONObject json = new JSONObject();
		json.put("nombreUsuario", nombreEnviador);
		json.put("mensaje", mensaje);
		json.put("url", url);
		
		template.convertAndSend("/usuario/" + nombreRecibidor, json.toString());
	}
	
}