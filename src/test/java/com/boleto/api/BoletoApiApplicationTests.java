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
	public void testIfBoletoEmDiaMultaIsNullTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(20)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isNull();
	}
	
	@Test
	public void calcularMultaMaisDe10DiasBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(20)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isEqualTo(10.0);
		assertThat(boleto.getTotal().doubleValue()).isEqualTo(110.00);
	}
	
	@Test
	public void calcularMultaMenos10DiasBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(5)); 
		this.service.calcularMulta(boleto);
		assertThat(boleto.getMulta()).isEqualTo(5.0);
		assertThat(boleto.getTotal().doubleValue()).isEqualTo(105.00);
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
	public void validarBoletoAtrasadoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(20));
		assertThat(boleto.isAtrasado()).isTrue();
	}
	
	@Test
	public void validarBoletoEmDiaTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(20));
		assertThat(boleto.isAtrasado()).isFalse();
	}
	
	@Test
	public void isNotCalculableBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now());
		assertThat(boleto.isCalculable()).isFalse();
		
		boleto.setStatus(EnumStatus.PAID);
		assertThat(boleto.isCalculable()).isFalse();
		
		boleto.setStatus(EnumStatus.PAID);
		assertThat(boleto.isCalculable()).isFalse();
	}
	
	@Test
	public void isCalculableBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().minusDays(12));
		assertThat(boleto.isCalculable()).isTrue();
	}
}
