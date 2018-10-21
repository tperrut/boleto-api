package com.boleto.api.dao;

import java.util.List;

import com.boleto.api.model.Boleto;

public interface BoletoDao {
	
	String salvar(Boleto Boleto);

	void editar(Boleto Boleto);

	void excluir(Long id);

	Boleto buscarPorId(String id);

	List<Boleto> buscarTodos();

}
