package com.boleto.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.boleto.api.dao.BoletoRepository;
import com.boleto.api.dto.DataDto;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;
import com.boleto.api.web.controller.BoletoController;
import com.boleto.api.web.exception.ResourceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BoletoEndPointTest {

	private static final String RESOURCE_NOT_FOUND = "Resource Not Found";

	private static final String ICN = "ICN";

	private static final String VALE = "VALE";

	public static final String CLIENTE_TESTE ="CLIENTE_TESTE";  
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private BoletoRepository boletoRepository;
	
	@Autowired
	private BoletoController controller;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void contextLoads() {
		System.out.println("Porta teste: "+port);
	}
	
	private Boleto createBoleto(LocalDate dataVencimento, String nome) {
		Boleto boleto = new Boleto(nome, dataVencimento, new BigDecimal(100));
		boleto.setStatus(EnumStatus.PENDING);
		boleto.setId(1L);
		return boleto;
	}
	
	@Test
	public void listBoletosTest() {
		List<Boleto> boletos = Arrays.asList(createBoleto(LocalDate.now(), VALE),createBoleto(LocalDate.now(), ICN));
		BDDMockito.when(boletoRepository.findAll()).thenReturn(boletos);
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/boletos",String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().contains(ICN)).isTrue();
		assertThat(response.getBody().contains(VALE)).isTrue();
	}
	
	@Test
	public void findByClienteNotValidTest() {
		Boleto boleto = createBoleto(LocalDate.now(), CLIENTE_TESTE);
		BDDMockito.when(boletoRepository.save(boleto)).thenReturn(boleto);
		
		Optional<Boleto> boletoOpt = Optional.of(boleto);
		BDDMockito.when(boletoRepository.findByCliente(CLIENTE_TESTE)).thenReturn(boletoOpt);
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/boletos/cliente/client_no_exist",String.class);
		assertThat(response.getBody().contains(RESOURCE_NOT_FOUND)).isTrue();
		assertThat(response.getStatusCodeValue()).isEqualTo(404);

	}
	
	//@Test
	public void pagarBoletoEmDiaTest() {
	}
	
	//@Test
	public void pagarBoletoEmAtrasoTest() {
	}
	
	//@Test
	public void pagarBoletoComMaisDe10DiasEmAtrasoTest() {
	}
	
	//@Test
	public void pagarBoletoComMenosDe10DiasEmAtrasoTest() {
	}
	
	@Test
	public void boletoNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.controller.verificarSeBoletoExiste(1898L);
	}
	
	@Test
	public void findByClientValidTest() {
		Boleto boleto = createBoleto(LocalDate.now(), CLIENTE_TESTE);
		BDDMockito.when(boletoRepository.save(boleto)).thenReturn(boleto);
		
		Optional<Boleto> boletoOpt= Optional.of(boleto);
		System.out.println("boletoOpt "+ boletoOpt.isPresent());
		BDDMockito.when(boletoRepository.findByCliente(CLIENTE_TESTE)).thenReturn(boletoOpt);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/boletos/cliente/"+CLIENTE_TESTE,String.class);

		assertThat(response.getBody().contains(CLIENTE_TESTE)).isTrue();
		assertThat(response.getBody().contains(ICN)).isFalse();
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
}
