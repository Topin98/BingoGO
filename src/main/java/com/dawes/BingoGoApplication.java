package com.dawes;

import javax.persistence.Persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BingoGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BingoGoApplication.class, args);
		Persistence.generateSchema("jpa", null);
	}

}

