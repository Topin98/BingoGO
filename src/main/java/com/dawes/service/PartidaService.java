package com.dawes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dawes.modelo.PartidaVO;

public interface PartidaService {

	<S extends PartidaVO> S save(S entity);

	<S extends PartidaVO> Optional<S> findOne(Example<S> example);

	Page<PartidaVO> findAll(Pageable pageable);

	List<PartidaVO> findAll();

	List<PartidaVO> findAll(Sort sort);

	Optional<PartidaVO> findById(Integer id);

	List<PartidaVO> findAllById(Iterable<Integer> ids);

	<S extends PartidaVO> List<S> saveAll(Iterable<S> entities);

	boolean existsById(Integer id);

	void flush();

	<S extends PartidaVO> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<PartidaVO> entities);

	<S extends PartidaVO> Page<S> findAll(Example<S> example, Pageable pageable);

	long count();

	void deleteAllInBatch();

	void deleteById(Integer id);

	PartidaVO getOne(Integer id);

	void delete(PartidaVO entity);

	<S extends PartidaVO> long count(Example<S> example);

	void deleteAll(Iterable<? extends PartidaVO> entities);

	<S extends PartidaVO> List<S> findAll(Example<S> example);

	<S extends PartidaVO> boolean exists(Example<S> example);

	void deleteAll();

	<S extends PartidaVO> List<S> findAll(Example<S> example, Sort sort);

}