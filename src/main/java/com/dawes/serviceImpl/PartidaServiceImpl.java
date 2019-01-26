package com.dawes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawes.modelo.PartidaVO;
import com.dawes.repository.PartidaRepository;
import com.dawes.service.PartidaService;

@Service
public class PartidaServiceImpl implements PartidaService {

	@Autowired
	PartidaRepository partidaRepository;

	@Override
	public <S extends PartidaVO> S save(S entity) {
		return partidaRepository.save(entity);
	}

	@Override
	public <S extends PartidaVO> Optional<S> findOne(Example<S> example) {
		return partidaRepository.findOne(example);
	}

	@Override
	public Page<PartidaVO> findAll(Pageable pageable) {
		return partidaRepository.findAll(pageable);
	}

	@Override
	public List<PartidaVO> findAll() {
		return partidaRepository.findAll();
	}

	@Override
	public List<PartidaVO> findAll(Sort sort) {
		return partidaRepository.findAll(sort);
	}

	@Override
	public Optional<PartidaVO> findById(Integer id) {
		return partidaRepository.findById(id);
	}

	@Override
	public List<PartidaVO> findAllById(Iterable<Integer> ids) {
		return partidaRepository.findAllById(ids);
	}

	@Override
	public <S extends PartidaVO> List<S> saveAll(Iterable<S> entities) {
		return partidaRepository.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return partidaRepository.existsById(id);
	}

	@Override
	public void flush() {
		partidaRepository.flush();
	}

	@Override
	public <S extends PartidaVO> S saveAndFlush(S entity) {
		return partidaRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<PartidaVO> entities) {
		partidaRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends PartidaVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return partidaRepository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return partidaRepository.count();
	}

	@Override
	public void deleteAllInBatch() {
		partidaRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Integer id) {
		partidaRepository.deleteById(id);
	}

	@Override
	public PartidaVO getOne(Integer id) {
		return partidaRepository.getOne(id);
	}

	@Override
	public void delete(PartidaVO entity) {
		partidaRepository.delete(entity);
	}

	@Override
	public <S extends PartidaVO> long count(Example<S> example) {
		return partidaRepository.count(example);
	}

	@Override
	public void deleteAll(Iterable<? extends PartidaVO> entities) {
		partidaRepository.deleteAll(entities);
	}

	@Override
	public <S extends PartidaVO> List<S> findAll(Example<S> example) {
		return partidaRepository.findAll(example);
	}

	@Override
	public <S extends PartidaVO> boolean exists(Example<S> example) {
		return partidaRepository.exists(example);
	}

	@Override
	public void deleteAll() {
		partidaRepository.deleteAll();
	}

	@Override
	public <S extends PartidaVO> List<S> findAll(Example<S> example, Sort sort) {
		return partidaRepository.findAll(example, sort);
	}
	
	
}
