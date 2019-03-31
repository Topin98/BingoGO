package com.dawes;

import javax.persistence.Persistence;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		Persistence.generateSchema("jpa", null);
		return application.sources(BingoGoApplication.class);
	}

}

