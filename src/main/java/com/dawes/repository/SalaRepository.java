package com.dawes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.SalaVO;

@Repository
public interface SalaRepository extends JpaRepository<SalaVO, Integer>{
	
}
