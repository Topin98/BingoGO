package com.dawes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.PremioVO;

@Repository
public interface PremioRepository extends JpaRepository<PremioVO, Integer>{
	
	PremioVO findByNombre(String nombre);
}
