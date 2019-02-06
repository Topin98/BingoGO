package com.dawes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import com.dawes.modelo.UsuarioVO;

public interface UsuarioService {

	<S extends UsuarioVO> S save(S entity);

	<S extends UsuarioVO> Optional<S> findOne(Example<S> example);

	Page<UsuarioVO> findAll(Pageable pageable);

	List<UsuarioVO> findAll();

	List<UsuarioVO> findAll(Sort sort);

	Optional<UsuarioVO> findById(Integer id);

	List<UsuarioVO> findAllById(Iterable<Integer> ids);

	<S extends UsuarioVO> List<S> saveAll(Iterable<S> entities);

	boolean existsById(Integer id);

	void flush();

	<S extends UsuarioVO> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<UsuarioVO> entities);

	<S extends UsuarioVO> Page<S> findAll(Example<S> example, Pageable pageable);

	long count();

	void deleteAllInBatch();

	void deleteById(Integer id);

	UsuarioVO getOne(Integer id);

	void delete(UsuarioVO entity);

	<S extends UsuarioVO> long count(Example<S> example);

	void deleteAll(Iterable<? extends UsuarioVO> entities);

	<S extends UsuarioVO> List<S> findAll(Example<S> example);

	<S extends UsuarioVO> boolean exists(Example<S> example);

	void deleteAll();

	<S extends UsuarioVO> List<S> findAll(Example<S> example, Sort sort);

	UsuarioVO findByNombre(String nombre);

	UsuarioVO findByCorreo(String correo);

	UserDetails loadUserByUsername(String nombre);

}