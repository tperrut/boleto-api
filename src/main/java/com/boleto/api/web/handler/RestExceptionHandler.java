package com.boleto.api.web.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.boleto.api.web.error.JsonNotReadableDetail;
import com.boleto.api.web.error.ResourceNotFoundDetails;
import com.boleto.api.web.error.ValidationErrorDetail;
import com.boleto.api.web.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class) 
	public ResponseEntity<?> handleResourceNotFoundException (ResourceNotFoundException e){
		ResourceNotFoundDetails ex = ResourceNotFoundDetails.builder().
		detalhe(e.getMessage()).
		developerMessage(ResourceNotFoundException.class.getName()).
		statusCode(HttpStatus.NOT_FOUND.value()).
		timestamp(new Date()).
		titulo("Resource Not Found").
		build();
		return new ResponseEntity<>(ex,HttpStatus.NOT_FOUND );
		
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		JsonNotReadableDetail jnr = JsonNotReadableDetail.builder().
				detalhe(ex.getMessage()).
				developerMessage(JsonNotReadableDetail.class.getName()).
				statusCode(HttpStatus.BAD_REQUEST.value()).
				timestamp(new Date()).
				titulo("Json is invalid").
				build();
				 
		
		return new ResponseEntity<>(jnr,HttpStatus.BAD_REQUEST);

	}
	
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fields = ex.getBindingResult().getFieldErrors();
		String fieldErro =  fields.stream().map(FieldError::getField).collect(Collectors.joining(" | "));
		String fieldMsg =  fields.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" | "));
		ValidationErrorDetail jnr = ValidationErrorDetail.builder().
				detalhe("Ver Fields").
				developerMessage(ValidationErrorDetail.class.getName()).
				statusCode(HttpStatus.BAD_REQUEST.value()).
				timestamp(new Date()).
				titulo("Erro de validação").
				field(fieldErro).
				fieldMessages(fieldMsg).
				build();
				 
		
		return new ResponseEntity<>(jnr,HttpStatus.BAD_REQUEST);

	}
	
	
}
