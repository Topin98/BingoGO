package com.dawes.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CartonVO {
	
	public static final int precio = 30; //el precio solo se aplica si es premium
	private static final int iNumeros = 15;
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
	
	private void generarCarton() {
		
		//numero que se generara aleatoriamente en bucle
		int num;
		
		//la cantidad de numeros que tendra el carton
		int cantidadNumeros = premium ? iNumeros - ThreadLocalRandom.current().nextInt(1, 5 + 1) : iNumeros;
		
		while (lNumeros.size() < cantidadNumeros) {
			
			num = ThreadLocalRandom.current().nextInt(numeroMenor, numeroMayor + 1);
			if (!lNumeros.contains(num)) lNumeros.add(num);
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
