package com.dawes.utils;

import java.time.LocalDate;

import com.dawes.modelo.PartidaVO;
import com.dawes.modelo.UsuarioVO;

public class PartidaUtils {
	
	public void transformarPartida(PartidaVO partida) {
		
		//en el caso de la partida solo hace falta ponerle la fecha
		partida.setFecha(LocalDate.now());
	}
	
	public boolean validarPartida(PartidaVO partida) {
		
		return partida.getIdPartida() != 0 && partida.getFecha() != null;
	}
}
