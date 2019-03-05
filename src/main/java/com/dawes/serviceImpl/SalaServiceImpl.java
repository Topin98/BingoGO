package com.dawes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawes.modelo.SalaVO;
import com.dawes.repository.SalaRepository;
import com.dawes.service.SalaService;

@Service
public class SalaServiceImpl implements SalaService {

	@Autowired
	SalaRepository salaRespository;

	@Override
	public <S extends SalaVO> S save(S entity) {
		return salaRespository.save(entity);
	}

	@Override
	public <S extends SalaVO> Optional<S> findOne(Example<S> example) {
		return salaRespository.findOne(example);
	}

	@Override
	public Page<SalaVO> findAll(Pageable pageable) {
		return salaRespository.findAll(pageable);
	}

	@Override
	public List<SalaVO> findAll() {
		return salaRespository.findAll();
	}

	@Override
	public List<SalaVO> findAll(Sort sort) {
		return salaRespository.findAll(sort);
	}

	@Override
	public Optional<SalaVO> findById(Integer id) {
		return salaRespository.findById(id);
	}

	@Override
	public List<SalaVO> findAllById(Iterable<Integer> ids) {
		return salaRespository.findAllById(ids);
	}

	@Override
	public <S extends SalaVO> List<S> saveAll(Iterable<S> entities) {
		return salaRespository.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return salaRespository.existsById(id);
	}

	@Override
	public void flush() {
		salaRespository.flush();
	}

	@Override
	public <S extends SalaVO> S saveAndFlush(S entity) {
		return salaRespository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<SalaVO> entities) {
		salaRespository.deleteInBatch(entities);
	}

	@Override
	public <S extends SalaVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return salaRespository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return salaRespository.count();
	}

	@Override
	public void deleteAllInBatch() {
		salaRespository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Integer id) {
		salaRespository.deleteById(id);
	}

	@Override
	public SalaVO getOne(Integer id) {
		return salaRespository.getOne(id);
	}

	@Override
	public void delete(SalaVO entity) {
		salaRespository.delete(entity);
	}

	@Override
	public <S extends SalaVO> long count(Example<S> example) {
		return salaRespository.count(example);
	}

	@Override
	public void deleteAll(Iterable<? extends SalaVO> entities) {
		salaRespository.deleteAll(entities);
	}

	@Override
	public <S extends SalaVO> List<S> findAll(Example<S> example) {
		return salaRespository.findAll(example);
	}

	@Override
	public <S extends SalaVO> boolean exists(Example<S> example) {
		return salaRespository.exists(example);
	}

	@Override
	public void deleteAll() {
		salaRespository.deleteAll();
	}

	@Override
	public <S extends SalaVO> List<S> findAll(Example<S> example, Sort sort) {
		return salaRespository.findAll(example, sort);
	}

	@Override
	public List<SalaVO> findByActiva(boolean activa) {
		return salaRespository.findByActiva(activa);
	}
	
}
