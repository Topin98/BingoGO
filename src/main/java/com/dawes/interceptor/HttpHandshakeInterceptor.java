package com.dawes.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.dawes.modelo.CartonVO;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) {
			
			//obtenemos la session a partir de la "session" ficticia del socket
			HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession();

 			//si la session tiene un atributo carton se lo pasamos a la "session" del socket
			//se usa para que nadie haga trampas cantando bingo o linea retocando el js
			CartonVO carton = (CartonVO) session.getAttribute("carton");
			if (carton != null) {
				attributes.put("carton", carton);
			}
		}

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		// System.out.println("entra2");
		// logger.info("Call afterHandshake");
	}

}