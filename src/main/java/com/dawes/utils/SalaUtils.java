package com.dawes.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dawes.modelo.SalaVO;
import com.dawes.modelo.UsuarioVO;

public class SalaUtils {

	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	public void transformarSala(SalaVO sala, UsuarioVO usuario) {
		
		//la pw llega como "", la convertimos a null
		if (sala.isPublica()) sala.setPassword(null);
		else sala.setPassword(pwEncoder.encode(sala.getPassword()));
		
		sala.setIdPropietario(usuario.getIdUsuario());
		
	}
}
