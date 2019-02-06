package com.dawes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.UsuarioVO;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioVO, Integer>{
	UsuarioVO findByNombre(String nombre);
	UsuarioVO findByCorreo(String correo);
}
