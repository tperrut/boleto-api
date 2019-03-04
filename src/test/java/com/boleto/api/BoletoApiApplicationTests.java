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
import com.boleto.api.web.controller.BoletoController;
import com.boleto.api.web.exception.ResourceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoletoApiApplicationTests {

	@Autowired
	private BoletoRepository repository;
	
	@Autowired
	private BoletoController controller;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void contextLoads() {
	}
	
	
	private Boleto createBoleto() {
		LocalDate dataVencimento = LocalDate.now().plusDays(12);
		Boleto boleto = new Boleto("VALE", dataVencimento, new BigDecimal(1000));
		boleto.setStatus(EnumStatus.PENDING);
		return boleto;
	}
	
	@Test
	public void criarBoletoTest() {
		Boleto boleto = createBoleto(); 
		this.repository.save(boleto);
		assertThat(boleto.getId()).isNotNull();
		assertThat(boleto.getCliente()).isEqualTo("VALE");
		assertThat(boleto.getStatus().id()).isEqualTo(EnumStatus.PENDING.id());
	}

	@Test
	public void boletoNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.controller.verificarSeBoletoExiste("test");
	}
	
	@Test
	public void alterarBoletoTest() {
		Boleto boleto = createBoleto();
		boleto = this.repository.save(boleto);
		boleto.setCliente("VALE NADA");
		boleto = this.repository.save(boleto);
		assertThat(boleto.getCliente()).isEqualTo("VALE NADA");
	}
	
	@Test
	public void deleteBoletoTest() {
		Boleto boleto = createBoleto();
		boleto = this.repository.save(boleto);
		String id = boleto.getId();
		this.repository.delete(boleto);
		Optional<Boleto> retorno =  this.repository.findById(id);
		assertThat(retorno.isPresent()).isFalse();
	}
	
	@Test
	public void pagarBoletoTest() {
		Boleto boleto = createBoleto();
		boleto = this.repository.save(boleto);
		boleto.setDataPagamento(LocalDate.now());
		boleto.setStatus(EnumStatus.PAID);
		boleto = this.repository.save(boleto);
		assertThat(boleto.getDataPagamento()).isNotNull();
		assertThat(boleto.getStatus()).isEqualTo(EnumStatus.PAID);
	}
	
	@Test
	public void validarBoletoAtrasadoTest() {
		Boleto boleto = createBoleto();
		boleto.setDataVencimento(LocalDate.now().minusDays(20));
		assertThat(boleto.isAtrasado()).isTrue();
	}
	
	@Test
	public void validarBoletoEmDiaTest() {
		Boleto boleto = createBoleto();
		boleto.setDataVencimento(LocalDate.now().minusDays(20));
		assertThat(boleto.isAtrasado()).isTrue();
	}
	
}
