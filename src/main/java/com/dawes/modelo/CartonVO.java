package com.dawes.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CartonVO {
	
	public static final int PRECIO = 30; //el precio solo se aplica si es premium
	private static final int numeroMenor = 1;
	private static final int numeroMayor = 90;
	
	private List<Integer> lNumeros;
	private boolean premium;
	
	public CartonVO() {
		super();
		lNumeros = new ArrayList<Integer>();
		//premium es falso por defecto
		
		generarCarton();
	}

	public CartonVO(boolean premium) {
		super();
		lNumeros = new ArrayList<Integer>();
		this.premium = premium;
		
		generarCarton();
		
	}
	
	//genera la lista de numeros del carton
	//que si que parece mucho misterio para generar 15 numeros random, pero mejor complicarlo aqui que en otro sitio
	private void generarCarton() {
		
		//numero que se generara aleatoriamente en bucle
		int num;
		
		//lista auxiliar en la que se añadiran 5 numeros aleatorios, despues 4 valores null y despues se barajara
		List<Integer> lFila;
		
		//para cada fila del carton (3 filas)
		for (int i = 0; i < 3; i++) {
			
			//generamos una nueva lista
			lFila = new ArrayList<Integer>();
			
			//hasta que no tenga 5 numeros
			while (lFila.size() < 5) {
				
				//generamos un numero aleatorio entre 1 y 90
				num = ThreadLocalRandom.current().nextInt(numeroMenor, numeroMayor + 1);
				
				//si el numero no lo contiene ni la fila actual ni ninguna de las anteriores lo añadimos
				if (!lNumeros.contains(num) && !lFila.contains(num)) lFila.add(num);
			}
			
			//añadimos 4 valores null
			for (int j = 0; j < 4; j++) lFila.add(null);
			
			//se baraja la fila
			Collections.shuffle(lFila);
			
			//se pasa la fila a la lista del carton
			lNumeros.addAll(lFila);
		}
		
		//si el carton es premium
		if (premium) {
		
			//quitamos entre 1 y 5 numeros del carton
			int numQuitar = ThreadLocalRandom.current().nextInt(1, 5 + 1);
			
			for (int i = 0; i < numQuitar; i++) {
				
				//sustituimos un numero al azar por null
				//obtenemos un item de la lista que no sea null al azar, sacamos su posicion y sustutuimos el valor del numero por null
				//si se usa stream() en vez de parallelStream() cogera siempre el primer numero, no cambiar
				lNumeros.set(lNumeros.indexOf(lNumeros.parallelStream().filter(x -> x != null).findAny().get()), null);
			}
		}
	}

	public List<Integer> getlNumeros() {
		return lNumeros;
	}

	public void setlNumeros(List<Integer> lNumeros) {
		this.lNumeros = lNumeros;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lNumeros == null) ? 0 : lNumeros.hashCode());
		result = prime * result + (premium ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartonVO other = (CartonVO) obj;
		if (lNumeros == null) {
			if (other.lNumeros != null)
				return false;
		} else if (!lNumeros.equals(other.lNumeros))
			return false;
		if (premium != other.premium)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CartonVO [lNumeros=" + lNumeros + " (Size=" + lNumeros.size() + ") " + ", premium=" + premium + "]";
	}
	
}
