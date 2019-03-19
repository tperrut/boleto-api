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

import com.boleto.api.web.error.BusinessExceptionDetail;
import com.boleto.api.web.error.InternalServerExceptionDetail;
import com.boleto.api.web.error.JsonNotReadableDetail;
import com.boleto.api.web.error.ResourceNotFoundDetails;
import com.boleto.api.web.error.ValidationErrorDetail;
import com.boleto.api.web.exception.BusinessException;
import com.boleto.api.web.exception.InternalServerException;
import com.boleto.api.web.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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
	
	@ExceptionHandler(BusinessException.class) 
	public ResponseEntity<?> handleResourceBusinessException (BusinessException e){
		BusinessExceptionDetail ex = BusinessExceptionDetail.builder().
		detalhe(e.getMessage()).
		developerMessage(BusinessException.class.getName()).
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
		timestamp(new Date()).
		titulo(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).
		build();
		return new ResponseEntity<>(ex,HttpStatus.UNPROCESSABLE_ENTITY );
		
	}
	@ExceptionHandler(InternalServerException.class) 
	public ResponseEntity<?> handleResourceInternalServerException (InternalServerException e){
		InternalServerExceptionDetail ex = InternalServerExceptionDetail.builder().
		detalhe(e.getMessage()).
		developerMessage(InternalServerException.class.getName()).
		statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
		timestamp(new Date()).
		titulo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).
		build();
		e.getCause().printStackTrace();
		return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String title = "Json is invalid";
		HttpStatus statusName = HttpStatus.UNPROCESSABLE_ENTITY;
		int statusCode = statusName.value();
		
		if(ex.getMessage().contains(" not a valid representation"))
			title = "Erro de Conversão";
		else if(ex.getMessage().contains("Required request body is missing")) {
			statusCode= HttpStatus.BAD_REQUEST.value();
			statusName = HttpStatus.BAD_REQUEST;
			title = "Corpo da Requisição vazio";
		}
		
		if(ex.getCause() instanceof InvalidFormatException) {
			title = "Formato  de  Data Inválida: default format yyyy-MM-dd";
		}
			
		
		JsonNotReadableDetail jnr = JsonNotReadableDetail.builder().
				detalhe(ex.getMessage().substring(0,50).concat(" ...")).
				developerMessage(JsonNotReadableDetail.class.getName()).
				statusCode(statusCode).
				timestamp(new Date()).
				titulo(title).
				build();
				 
		ex.printStackTrace();
		return new ResponseEntity<>(jnr,statusName);

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
				statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
				timestamp(new Date()).
				titulo(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).
				field(fieldErro).
				fieldMessages(fieldMsg).
				build();
		ex.printStackTrace();		 
		return new ResponseEntity<>(jnr,status);
		

	}
	
	
}
