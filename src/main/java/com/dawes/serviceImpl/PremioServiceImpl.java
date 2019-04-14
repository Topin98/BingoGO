package com.dawes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawes.modelo.PremioVO;
import com.dawes.repository.PremioRepository;
import com.dawes.service.PremioService;

@Service
public class PremioServiceImpl implements PremioService {

	@Autowired
	PremioRepository premioRepository;

	@Override
	public <S extends PremioVO> S save(S entity) {
		return premioRepository.save(entity);
	}

	@Override
	public <S extends PremioVO> Optional<S> findOne(Example<S> example) {
		return premioRepository.findOne(example);
	}

	@Override
	public Page<PremioVO> findAll(Pageable pageable) {
		return premioRepository.findAll(pageable);
	}

	@Override
	public List<PremioVO> findAll() {
		return premioRepository.findAll();
	}

	@Override
	public List<PremioVO> findAll(Sort sort) {
		return premioRepository.findAll(sort);
	}

	@Override
	public Optional<PremioVO> findById(Integer id) {
		return premioRepository.findById(id);
	}

	@Override
	public List<PremioVO> findAllById(Iterable<Integer> ids) {
		return premioRepository.findAllById(ids);
	}

	@Override
	public <S extends PremioVO> List<S> saveAll(Iterable<S> entities) {
		return premioRepository.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return premioRepository.existsById(id);
	}

	@Override
	public void flush() {
		premioRepository.flush();
	}

	@Override
	public <S extends PremioVO> S saveAndFlush(S entity) {
		return premioRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<PremioVO> entities) {
		premioRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends PremioVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return premioRepository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return premioRepository.count();
	}

	@Override
	public void deleteAllInBatch() {
		premioRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Integer id) {
		premioRepository.deleteById(id);
	}

	@Override
	public PremioVO getOne(Integer id) {
		return premioRepository.getOne(id);
	}

	@Override
	public void delete(PremioVO entity) {
		premioRepository.delete(entity);
	}

	@Override
	public <S extends PremioVO> long count(Example<S> example) {
		return premioRepository.count(example);
	}

	@Override
	public void deleteAll(Iterable<? extends PremioVO> entities) {
		premioRepository.deleteAll(entities);
	}

	@Override
	public <S extends PremioVO> List<S> findAll(Example<S> example) {
		return premioRepository.findAll(example);
	}

	@Override
	public <S extends PremioVO> boolean exists(Example<S> example) {
		return premioRepository.exists(example);
	}

	@Override
	public void deleteAll() {
		premioRepository.deleteAll();
	}

	@Override
	public <S extends PremioVO> List<S> findAll(Example<S> example, Sort sort) {
		return premioRepository.findAll(example, sort);
	}

	@Override
	public PremioVO findByNombre(String nombre) {
		return premioRepository.findByNombre(nombre);
	}
	
}
