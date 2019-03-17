package com.boleto.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.model.Boleto;

@Service 
public class BoletoServiceImpl implements BoletoService {
	
	private static final double ATE_10_DIAS_ATRASO = 0.05;
	private static final double ACIMA_10_DIAS_ATRASO = 0.1;
	
	@Autowired
	private BoletoRepository dao;
	
	@Override @Transactional(propagation = Propagation.REQUIRED)
	public Boleto salvar(Boleto boleto) {
		return dao.save(boleto);
	}

	@Override 
	public void excluir(Long id) {}

	
	@Override	@Transactional(readOnly = true)
	public Optional<Boleto> buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Override @Transactional(readOnly = true)
	public List<Boleto> buscarTodos() {
		return dao.findAll();
	}

	@Override 
	public Boleto calcularMulta(Boleto boleto) {
		Double multa;
		
		if(isMenorOuIgualDezDias( boleto.getDataVencimento())){
			multa = getValorMulta(boleto,ATE_10_DIAS_ATRASO);
			aplicarMulta(boleto, multa);
		}else {
			multa = getValorMulta(boleto,ACIMA_10_DIAS_ATRASO);
			aplicarMulta(boleto, multa);
		}
				
		boleto.setMulta(multa);
		return boleto;
	}

	private Double getValorMulta(Boleto boleto,Double constante) {
		return boleto.getTotal().doubleValue() * constante ;
	}

	private void aplicarMulta(Boleto boleto, Double multa) {
		boleto.setTotal(boleto.getTotal().add(new BigDecimal(multa)).setScale(2,RoundingMode.HALF_DOWN));
	}
	
	private Boolean isMenorOuIgualDezDias(LocalDate dataVencimento) {
		Period periodo = Period.between(LocalDate.now(), dataVencimento);
		return periodo.getDays() <= 10;
	}

}
