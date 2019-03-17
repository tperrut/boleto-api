package com.boleto.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
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
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BoletoControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private BoletoRepository boletoRepository;
	
	
	private Boleto createBoleto(LocalDate dataVencimento, String nome) {
		Boleto boleto = new Boleto(nome, dataVencimento, new BigDecimal(100));
		boleto.setStatus(EnumStatus.PENDING);
		return boleto;
	}
	
	@Test
	public void listBoletosTest() {
		System.out.println("Porta teste: "+port);
		List<Boleto> boletos = Arrays.asList(createBoleto(LocalDate.now(), "VALE"),createBoleto(LocalDate.now(), "ICN"));
		BDDMockito.when(boletoRepository.findAll()).thenReturn(boletos);
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/boleto",String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		
	}
	@Test
	public void listBoletosByNameTest() {
		System.out.println("Porta teste: "+port);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/boleto",String.class);
		assertThat(response.getBody().contains("VALE")).isTrue();
	}
}
