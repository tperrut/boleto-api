package com.boleto.api.web.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.boleto.api.web.error.ResourceNotFoundDetails;
import com.boleto.api.web.error.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class) 
	public ResponseEntity<?> handleRscNtfExc(ResourceNotFoundException e){
		ResourceNotFoundDetails ex = ResourceNotFoundDetails.builder().
		detalhe(e.getMessage()).
		developerMessage(ResourceNotFoundException.class.getName()).
		statusCode(HttpStatus.NOT_FOUND.value()).
		timestamp(new Date().getTime()).
		titulo("Resource Not Found").
		build();
		return new ResponseEntity<>(ex,HttpStatus.NOT_FOUND );
		
	}
}
