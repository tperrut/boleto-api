package com.boleto.api.web.exception;

public class InternalServerException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InternalServerException(String msg) {
		super(msg);
	}
}
	