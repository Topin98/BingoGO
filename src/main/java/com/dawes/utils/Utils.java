package com.dawes.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

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
}