package com.boleto.api.web.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.boleto.api.model.Boleto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApi<T> {
	
	private List<T> data;
	private Page<Boleto> dataPaged;
	private Set<String> erros = new HashSet<String>();
	
}
