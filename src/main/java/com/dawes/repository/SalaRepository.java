package com.dawes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.SalaVO;

@Repository
public interface SalaRepository extends JpaRepository<SalaVO, Integer>{
	
	//consulta para el filtro de salas, filtra por nombre, estado de la sala (jugando) y privacidad (publica)
	@Query("select s from SalaVO s where s.nombre like %:nombre% and (s.jugando = :jugando or s.jugando = false) and (s.publica = :publica or publica = true)")
	Page<SalaVO> getSalasFiltradas(Pageable pageable, String nombre, boolean jugando, boolean publica);
	
	//idSala in (select count(*)????? pepoweird
	@Query(value = "select idSala, idPropietario, nombre, jugando, publica, password from salas where jugando = false and publica = true and idSala in (select count(*) from salas, usuarios where usuarios.idSala = salas.idSala) < :capMax order by rand() limit 1", nativeQuery = true)
	SalaVO getSalaAleatoria(int capMax);
	
}
