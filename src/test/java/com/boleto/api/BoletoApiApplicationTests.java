package com.boleto.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;
import com.boleto.api.service.BoletoServiceImpl;
import com.boleto.api.web.controller.BoletoController;
import com.boleto.api.web.exception.ResourceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoletoApiApplicationTests {

	@Autowired
	private BoletoRepository repository;
	
	@Autowired
	private BoletoController controller;
	
	@Autowired
	private BoletoServiceImpl service;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void contextLoads() {
	}
	
	
	private Boleto createBoleto(LocalDate dataVencimento) {
		Boleto boleto = new Boleto("VALE", dataVencimento, new BigDecimal(100));
		boleto.setStatus(EnumStatus.PENDING);
		return boleto;
	}
	

	@Test
	public void calcularMultaBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(20)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isNotNull();
	}
	
	@Test
	public void calcularMultaMaisDe10DiasBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(20)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isEqualTo(10.0);
		assertThat(boleto.getTotal().doubleValue()).isEqualTo(110.00);
	}
	
	@Test
	public void calcularMultaMenos10DiasBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(5)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isEqualTo(5.0);
		assertThat(boleto.getTotal().doubleValue()).isEqualTo(105.00);
	}
	
	
	@Test
	public void detalharBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(22)); 
		this.repository.save(boleto);
		Optional<Boleto> retorno =  this.repository.findById(boleto.getId());
		assertThat(retorno.isPresent()).isTrue();
		this.service.calcularMulta(retorno.get());
		assertThat(retorno.get().getMulta()).isNotNull();
	}

	
	@Test
	public void criarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12)); 
		this.repository.save(boleto);
		assertThat(boleto.getId()).isNotNull();
		assertThat(boleto.getMulta()).isNull();
		assertThat(boleto.getCliente()).isEqualTo("VALE");
		assertThat(boleto.getStatus().id()).isEqualTo(EnumStatus.PENDING.id());
	}

	@Test
	public void boletoNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.controller.verificarSeBoletoExiste(1898L);
	}
	
	@Test
	public void alterarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12));
		boleto = this.repository.save(boleto);
		boleto.setCliente("VALE NADA");
		boleto = this.repository.save(boleto);
		assertThat(boleto.getCliente()).isEqualTo("VALE NADA");
	}
	
	@Test
	public void deleteBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12));
		boleto = this.repository.save(boleto);
		Long id = boleto.getId();
		this.repository.delete(boleto);
		Optional<Boleto> retorno =  this.repository.findById(id);
		assertThat(retorno.isPresent()).isFalse();
	}
	
	
	//TODO remover esse teste e
	//mover para a classe de testes que é responsavel por testar o controller/endpoint 
	@Test
	public void pagarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12));
		boleto = this.repository.save(boleto);
		boleto.setDataPagamento(LocalDate.now());
		boleto.setStatus(EnumStatus.PAID);
		boleto = this.repository.save(boleto);
		assertThat(boleto.getDataPagamento()).isNotNull();
		assertThat(boleto.getStatus()).isEqualTo(EnumStatus.PAID);
	}
	
	@Test
	public void validarBoletoAtrasadoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(20));
		assertThat(boleto.isAtrasado()).isTrue();
	}
	
	@Test
	public void validarBoletoEmDiaTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(20));
		assertThat(boleto.isAtrasado()).isFalse();
	}
	
}
