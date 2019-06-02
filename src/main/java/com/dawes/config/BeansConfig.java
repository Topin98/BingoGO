package com.dawes.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.dawes.utils.PartidaUtils;
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
	
	@Bean
	public PartidaUtils getPartidaUtils() {
		return new PartidaUtils();
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("localhost");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("username");
	    mailSender.setPassword("pw");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	     
	    return mailSender;
	}
}
