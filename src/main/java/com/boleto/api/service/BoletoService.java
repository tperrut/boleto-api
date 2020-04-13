package com.boleto.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.boleto.api.model.Boleto;

public interface BoletoService {
	
	Boleto salvar(Boleto Boleto);
	
	void excluir(Long id);

	Optional<Boleto> buscarPorId(Long id);

	Page<Boleto> findAll(Pageable pageable);
	
	Boleto calcularMulta(Boleto Boleto);

	Optional<List<Boleto>> buscarPorCliente(String name);

	List<Boleto> findAll();
	
}
