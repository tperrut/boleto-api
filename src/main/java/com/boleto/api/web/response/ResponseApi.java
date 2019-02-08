package com.boleto.api.web.response;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApi<T> {
	
	private T data;
	private Set<String> erros = new HashSet<String>();
	
}
