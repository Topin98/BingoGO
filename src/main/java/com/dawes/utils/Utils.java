package com.dawes.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.security.core.Authentication;

public class Utils {

	//comprueba que una cadena no sea null ni esta vacia
	public static boolean validarCadena(String cadena) {
		return cadena != null && !cadena.trim().isEmpty();
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
}