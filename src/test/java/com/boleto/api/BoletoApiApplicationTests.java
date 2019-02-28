package com.boleto.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoletoApiApplicationTests {

	@Autowired
	private BoletoRepository repository;
	
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void contextLoads() {
	}
	
	
	@Test
	public void criarBoletoTest() {
		Boleto boleto = new Boleto("VALE", LocalDate.now().plusDays(12), new BigDecimal(1000));
		boleto.setStatus(EnumStatus.PENDING);
		this.repository.save(boleto);
		assertThat(boleto.getId()).isNotNull();
		assertThat(boleto.getCliente()).isEqualTo("VALE");
		assertThat(boleto.getStatus().id()).isEqualTo(EnumStatus.PENDING.id());
	}
	
	@Test
	public void verificarSeBoletoAtrasadoTest() {
		Boleto boleto = new Boleto("VALE", LocalDate.now().plusDays(20), new BigDecimal(1000));
		
		this.repository.save(boleto);
		assertThat(boleto.isAtrasado()).isFalse();
	}
	
}
