package com.dawes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.PartidaVO;

@Repository
public interface PartidaRepository extends JpaRepository<PartidaVO, Integer>{
	
}
