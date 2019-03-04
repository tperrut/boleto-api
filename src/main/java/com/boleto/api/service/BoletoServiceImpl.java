package com.boleto.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.model.Boleto;

@Service @Transactional(readOnly = false)
public class BoletoServiceImpl implements BoletoService {
	
	@Autowired
	private BoletoRepository dao;
	
	@Override
	public Boleto salvar(Boleto boleto) {
		return dao.save(boleto);
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

	@Override
	public Boleto calcularMulta(Boleto boleto) {
		LocalDate hoje = LocalDate.now();
		LocalDate dataVencimento = boleto.getDataVencimento();
		Double multa;
		
		if(isMenorOuIgualDezDias(hoje, dataVencimento)){
			
			multa = boleto.getTotal().doubleValue() * 0.05;
			boleto.setTotal(boleto.getTotal().add(new BigDecimal(multa)).setScale(2,RoundingMode.HALF_DOWN));
		}else {
			multa = boleto.getTotal().doubleValue() * 0.1;
			boleto.setTotal(boleto.getTotal().add(new BigDecimal(multa)).setScale(2,RoundingMode.HALF_DOWN));
		}
				
		boleto.setMulta(multa);
		return boleto;
	}
	
	private Boolean isMenorOuIgualDezDias(LocalDate hoje, LocalDate dataVencimento) {
		Period periodo = Period.between(hoje, dataVencimento);
		return periodo.getDays() <= 10;
	}

}
