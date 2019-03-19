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

	private static final String CLIENTE_FAIL = "VALE NADA";

	public final String CLIENTE_TESTE = "VALE";

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
		Boleto boleto = new Boleto(CLIENTE_TESTE, dataVencimento, new BigDecimal(100));
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
		assertThat(boleto.getCliente()).isEqualTo(CLIENTE_TESTE);
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
		boleto.setCliente(CLIENTE_FAIL);
		boleto = this.repository.save(boleto);
		assertThat(boleto.getCliente()).isEqualTo(CLIENTE_FAIL);
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
