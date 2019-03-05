package com.dawes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.SalaVO;

@Repository
public interface SalaRepository extends JpaRepository<SalaVO, Integer>{
	
	List<SalaVO> findByActiva(boolean activa);
}
