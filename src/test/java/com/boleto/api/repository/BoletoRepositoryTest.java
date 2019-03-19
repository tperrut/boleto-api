package com.boleto.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoletoRepositoryTest {
	
	private static final String TESTE_PAGO = "TESTE_PAGO";

	private static final String CLIENTE_NÃO_PODE_SER_VAZIO = "Cliente não pode ser vazio";

	private static final String CLIENTE_TESTE = "TESTE";

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
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12),CLIENTE_TESTE);
		
		Boleto resposta = repository.save(boleto);
		assertThat(resposta).isNotNull();
		assertThat(resposta.getCliente()).isEqualTo(CLIENTE_TESTE);
		assertThat(resposta.getStatus()).isEqualTo(EnumStatus.PENDING);
	}
	
	@Test
	public void criarComClienteIsNullThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage(CLIENTE_NÃO_PODE_SER_VAZIO);
		repository.save(new Boleto());
	}
	
	@Test
	public void pagarBoletoTest() {
		Boleto boleto = createBoleto(LocalDate.now().plusDays(12),CLIENTE_TESTE);
		
		repository.save(boleto);
		
		Optional<Boleto> opt = repository.findById(boleto.getId());

		if(opt.isPresent()) 
			boleto = opt.get();
		
		boleto.setStatus(EnumStatus.PAID);
		boleto.setCliente(TESTE_PAGO);
		
		Boleto resposta = repository.save(boleto);
		assertThat(resposta).isNotNull();
		assertThat(resposta.getCliente()).isEqualTo(TESTE_PAGO);
		assertThat(resposta.getStatus()).isEqualTo(EnumStatus.PAID);
	}
	
	

}









