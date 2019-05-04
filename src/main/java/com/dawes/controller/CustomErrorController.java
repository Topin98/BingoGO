package com.dawes.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dawes.utils.RR;

@Controller
@ControllerAdvice
public class CustomErrorController implements ErrorController  {
 
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		String resultado;
		
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	     
	        if (statusCode == HttpStatus.NOT_FOUND.value()) {
				resultado = "error404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				resultado = "error500";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				resultado = "error403";
			} else {
				resultado = "error";
			}
	        
	    } else {
	    	resultado = "error";
	    }
	    
	    return RR.CARPETA_ERRORES + resultado;
	}
	
	@ExceptionHandler(MultipartException.class)
    public String imagenNoValida() {
		
        return RR.CARPETA_ERRORES + "imagenGrande";

    }
	
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
