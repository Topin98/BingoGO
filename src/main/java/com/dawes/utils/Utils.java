package com.dawes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

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
}