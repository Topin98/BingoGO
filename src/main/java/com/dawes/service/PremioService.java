package com.dawes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dawes.modelo.PremioVO;

public interface PremioService {

	<S extends PremioVO> S save(S entity);

	<S extends PremioVO> Optional<S> findOne(Example<S> example);

	Page<PremioVO> findAll(Pageable pageable);

	List<PremioVO> findAll();

	List<PremioVO> findAll(Sort sort);

	Optional<PremioVO> findById(Integer id);

	List<PremioVO> findAllById(Iterable<Integer> ids);

	<S extends PremioVO> List<S> saveAll(Iterable<S> entities);

	boolean existsById(Integer id);

	void flush();

	<S extends PremioVO> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<PremioVO> entities);

	<S extends PremioVO> Page<S> findAll(Example<S> example, Pageable pageable);

	long count();

	void deleteAllInBatch();

	void deleteById(Integer id);

	PremioVO getOne(Integer id);

	void delete(PremioVO entity);

	<S extends PremioVO> long count(Example<S> example);

	void deleteAll(Iterable<? extends PremioVO> entities);

	<S extends PremioVO> List<S> findAll(Example<S> example);

	<S extends PremioVO> boolean exists(Example<S> example);

	void deleteAll();

	<S extends PremioVO> List<S> findAll(Example<S> example, Sort sort);

}