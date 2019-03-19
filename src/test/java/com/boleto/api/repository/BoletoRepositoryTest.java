package com.boleto.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.validation.ConstraintViolationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;

import ch.qos.logback.core.status.OnPrintStreamStatusListenerBase;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoletoRepositoryTest {
	
	@Autowired
	private BoletoRepository repository;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	private Boleto createBoleto(LocalDate dataVencimento, String nome) {
		Boleto boleto = new Boleto(nome, dataVencimento, new BigDecimal(100));
		boleto.setStatus(EnumStatus.PENDING);
		return boleto;
	}
	
	@Test
	public void criarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12),"TESTE");
		
		repository.save(boleto);
		assertThat(boleto).isNotNull();
		assertThat(boleto.getCliente()).isEqualTo("TESTE");
		assertThat(boleto.getStatus()).isEqualTo(EnumStatus.PENDING);
	}
	@Test
	public void criarComClienteIsNullThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("Cliente não pode ser vazio");
		repository.save(new Boleto());
	}
	
	@Test
	public void pagarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12),"TESTE");
		
		repository.save(boleto);
		
		Optional<Boleto> opt = repository.findById(boleto.getId());

		if(opt.isPresent()) 
			boleto = opt.get();
		
		boleto.setStatus(EnumStatus.PAID);
		boleto.setCliente("TESTE_PAGO");
		repository.save(boleto);
		
		assertThat(boleto).isNotNull();
		assertThat(boleto.getCliente()).isEqualTo("TESTE_PAGO");
		assertThat(boleto.getStatus()).isEqualTo(EnumStatus.PAID);
		
	}
	
	

}









