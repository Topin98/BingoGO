package com.dawes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawes.modelo.RolVO;
import com.dawes.repository.RolRepository;
import com.dawes.service.RolService;

@Service
public class RolServiceImpl implements RolService {

	@Autowired
	RolRepository rolRepository;

	@Override
	public <S extends RolVO> S save(S entity) {
		return rolRepository.save(entity);
	}

	@Override
	public <S extends RolVO> Optional<S> findOne(Example<S> example) {
		return rolRepository.findOne(example);
	}

	@Override
	public Page<RolVO> findAll(Pageable pageable) {
		return rolRepository.findAll(pageable);
	}

	@Override
	public List<RolVO> findAll() {
		return rolRepository.findAll();
	}

	@Override
	public List<RolVO> findAll(Sort sort) {
		return rolRepository.findAll(sort);
	}

	@Override
	public Optional<RolVO> findById(Integer id) {
		return rolRepository.findById(id);
	}

	@Override
	public List<RolVO> findAllById(Iterable<Integer> ids) {
		return rolRepository.findAllById(ids);
	}

	@Override
	public <S extends RolVO> List<S> saveAll(Iterable<S> entities) {
		return rolRepository.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return rolRepository.existsById(id);
	}

	@Override
	public void flush() {
		rolRepository.flush();
	}

	@Override
	public <S extends RolVO> S saveAndFlush(S entity) {
		return rolRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<RolVO> entities) {
		rolRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends RolVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return rolRepository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return rolRepository.count();
	}

	@Override
	public void deleteAllInBatch() {
		rolRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Integer id) {
		rolRepository.deleteById(id);
	}

	@Override
	public RolVO getOne(Integer id) {
		return rolRepository.getOne(id);
	}

	@Override
	public void delete(RolVO entity) {
		rolRepository.delete(entity);
	}

	@Override
	public <S extends RolVO> long count(Example<S> example) {
		return rolRepository.count(example);
	}

	@Override
	public void deleteAll(Iterable<? extends RolVO> entities) {
		rolRepository.deleteAll(entities);
	}

	@Override
	public <S extends RolVO> List<S> findAll(Example<S> example) {
		return rolRepository.findAll(example);
	}

	@Override
	public <S extends RolVO> boolean exists(Example<S> example) {
		return rolRepository.exists(example);
	}

	@Override
	public void deleteAll() {
		rolRepository.deleteAll();
	}

	@Override
	public <S extends RolVO> List<S> findAll(Example<S> example, Sort sort) {
		return rolRepository.findAll(example, sort);
	}
	
}
