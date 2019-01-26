package com.dawes.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawes.modelo.UsuarioVO;
import com.dawes.repository.RolRepository;
import com.dawes.repository.UsuarioRepository;
import com.dawes.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	public <S extends UsuarioVO> S save(S entity) {
		return usuarioRepository.save(entity);
	}

	@Override
	public <S extends UsuarioVO> Optional<S> findOne(Example<S> example) {
		return usuarioRepository.findOne(example);
	}

	@Override
	public Page<UsuarioVO> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

	@Override
	public List<UsuarioVO> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public List<UsuarioVO> findAll(Sort sort) {
		return usuarioRepository.findAll(sort);
	}

	@Override
	public Optional<UsuarioVO> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public List<UsuarioVO> findAllById(Iterable<Integer> ids) {
		return usuarioRepository.findAllById(ids);
	}

	@Override
	public <S extends UsuarioVO> List<S> saveAll(Iterable<S> entities) {
		return usuarioRepository.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return usuarioRepository.existsById(id);
	}

	@Override
	public void flush() {
		usuarioRepository.flush();
	}

	@Override
	public <S extends UsuarioVO> S saveAndFlush(S entity) {
		return usuarioRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<UsuarioVO> entities) {
		usuarioRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends UsuarioVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return usuarioRepository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return usuarioRepository.count();
	}

	@Override
	public void deleteAllInBatch() {
		usuarioRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Integer id) {
		usuarioRepository.deleteById(id);
	}

	@Override
	public UsuarioVO getOne(Integer id) {
		return usuarioRepository.getOne(id);
	}

	@Override
	public void delete(UsuarioVO entity) {
		usuarioRepository.delete(entity);
	}

	@Override
	public <S extends UsuarioVO> long count(Example<S> example) {
		return usuarioRepository.count(example);
	}

	@Override
	public void deleteAll(Iterable<? extends UsuarioVO> entities) {
		usuarioRepository.deleteAll(entities);
	}

	@Override
	public <S extends UsuarioVO> List<S> findAll(Example<S> example) {
		return usuarioRepository.findAll(example);
	}

	@Override
	public <S extends UsuarioVO> boolean exists(Example<S> example) {
		return usuarioRepository.exists(example);
	}

	@Override
	public void deleteAll() {
		usuarioRepository.deleteAll();
	}

	@Override
	public <S extends UsuarioVO> List<S> findAll(Example<S> example, Sort sort) {
		return usuarioRepository.findAll(example, sort);
	}

	@Override
	public UsuarioVO findByNombre(String nombre) {
		return usuarioRepository.findByNombre(nombre);
	}

	@Override
	public UserDetails loadUserByUsername(String nombre) {
		
		UsuarioVO usuario = usuarioRepository.findByNombre(nombre);
		
		if (usuario != null) {
			
			//estas dos lineas se supone que luego valdran para pillar los roles
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
	        grantedAuthorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));
	        
			User user = new User(usuario.getNombre(), usuario.getPassword(), grantedAuthorities);
			
			return user;
		} else {
			throw new UsernameNotFoundException("Error");
		}
        
        
    }
}
