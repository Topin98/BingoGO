package com.dawes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.UsuarioVO;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioVO, Integer>{
	
	UsuarioVO findByNombre(String nombre);
	UsuarioVO findByCorreo(String correo);
	
	//obtiene los 100 usuarios con mejor puntuacion
	List<UsuarioVO> findTop100ByOrderByPuntuacionTotalDesc();
	
	//obtiene los 100 usuarios con mas fichas
	List<UsuarioVO> findTop100ByOrderByFichasDesc();
	
	//obtiene los 100 usuarios que mas victorias tienen
	//devuelve una lista de objetos que contienen el nombre y el numero de victorias
	@Query(value = "select nombre, count(*) from usuarios, linusupar where usuarios.idUsuario = linusupar.usuario_idUsuario and linusupar.posicionBingo = 1 group by usuarios.nombre order by count(*) desc limit 100", nativeQuery = true)
	List<Object[]> getTop100UsuVictorias();
	
	//filtra a los usuarios por sus nombres
	Page<UsuarioVO> findAllByNombreContaining(Pageable pageable, String nombre);
	
}
