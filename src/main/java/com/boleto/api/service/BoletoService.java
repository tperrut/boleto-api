package com.boleto.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.boleto.api.model.Boleto;

public interface BoletoService {
	
	Boleto salvar(Boleto Boleto);
	
	void excluir(Long id);

	Optional<Boleto> buscarPorId(Long id);

	List<Boleto> buscarTodos();
	
	Boleto calcularMulta(Boleto Boleto);
	
}
