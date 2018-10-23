package com.boleto.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boleto.api.dao.BoletoDaoImpl;
import com.boleto.api.model.Boleto;

@Service @Transactional(readOnly = false)
public class BoletoServiceImpl implements BoletoService {
	
	@Autowired
	private BoletoDaoImpl dao;
	
	@Override
	public Boleto salvar(Boleto boleto) {
		return dao.save(boleto);
	}

	@Override
	public void editar(Boleto Boleto) {
		
	}

	@Override
	public void excluir(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Boleto> buscarPorId(String id) {
		return dao.findById(id);
	}

	@Override
	public List<Boleto> buscarTodos() {
		return dao.findAll();
	}

}
