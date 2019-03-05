package com.dawes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dawes.utils.SalaUtils;
import com.dawes.utils.UsuarioUtils;

@Configuration
public class BeansConfig {

	@Bean
	public UsuarioUtils getUsuarioUtils() {
		return new UsuarioUtils();
	}
	
	@Bean
	public SalaUtils getSalaUtils() {
		return new SalaUtils();
	}
}
