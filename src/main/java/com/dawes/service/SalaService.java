package com.dawes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dawes.modelo.SalaVO;

public interface SalaService {

	<S extends SalaVO> S save(S entity);

	<S extends SalaVO> Optional<S> findOne(Example<S> example);

	Page<SalaVO> findAll(Pageable pageable);

	List<SalaVO> findAll();

	List<SalaVO> findAll(Sort sort);

	Optional<SalaVO> findById(Integer id);

	List<SalaVO> findAllById(Iterable<Integer> ids);

	<S extends SalaVO> List<S> saveAll(Iterable<S> entities);

	boolean existsById(Integer id);

	void flush();

	<S extends SalaVO> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<SalaVO> entities);

	<S extends SalaVO> Page<S> findAll(Example<S> example, Pageable pageable);

	long count();

	void deleteAllInBatch();

	void deleteById(Integer id);

	SalaVO getOne(Integer id);

	void delete(SalaVO entity);

	<S extends SalaVO> long count(Example<S> example);

	void deleteAll(Iterable<? extends SalaVO> entities);

	<S extends SalaVO> List<S> findAll(Example<S> example);

	<S extends SalaVO> boolean exists(Example<S> example);

	void deleteAll();

	<S extends SalaVO> List<S> findAll(Example<S> example, Sort sort);
	
	Page<SalaVO> getSalasFiltradas(Pageable pageable, String nombre, boolean jugando, boolean publica);
	
	SalaVO getSalaAleatoria(int capMax);

}