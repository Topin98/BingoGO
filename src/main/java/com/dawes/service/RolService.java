package com.dawes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dawes.modelo.RolVO;

public interface RolService {

	<S extends RolVO> S save(S entity);

	<S extends RolVO> Optional<S> findOne(Example<S> example);

	Page<RolVO> findAll(Pageable pageable);

	List<RolVO> findAll();

	List<RolVO> findAll(Sort sort);

	Optional<RolVO> findById(Integer id);

	List<RolVO> findAllById(Iterable<Integer> ids);

	<S extends RolVO> List<S> saveAll(Iterable<S> entities);

	boolean existsById(Integer id);

	void flush();

	<S extends RolVO> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<RolVO> entities);

	<S extends RolVO> Page<S> findAll(Example<S> example, Pageable pageable);

	long count();

	void deleteAllInBatch();

	void deleteById(Integer id);

	RolVO getOne(Integer id);

	void delete(RolVO entity);

	<S extends RolVO> long count(Example<S> example);

	void deleteAll(Iterable<? extends RolVO> entities);

	<S extends RolVO> List<S> findAll(Example<S> example);

	<S extends RolVO> boolean exists(Example<S> example);

	void deleteAll();

	<S extends RolVO> List<S> findAll(Example<S> example, Sort sort);

}