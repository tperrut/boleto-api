package com.boleto.api.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.boleto.api.dto.BoletoDto;

public class BoletoControllerTest extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getBoletosListEmpty() throws Exception {
	   String uri = "/rest/boletos";
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE))
		  .andReturn();
	   
	   int status = mvcResult.getResponse().getStatus();
	   assertEquals(200, status);
	}
	
	
	@Test
	public void createBoleto() throws Exception {
	   String uri = "/rest/boletos";
	   BoletoDto dto = new BoletoDto("BOLETO_TESTE",LocalDate.now(), new BigDecimal(1000));
	   
	   String inputJson = super.mapToJson(dto);
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
	      .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
	   
	   int status = mvcResult.getResponse().getStatus();
	   assertEquals(201, status);
	   //String content = mvcResult.getResponse().getContentAsString();	   assertEquals(content, "Product is created successfully");
	}
	
}
