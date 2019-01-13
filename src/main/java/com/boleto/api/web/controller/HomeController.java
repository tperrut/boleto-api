package com.boleto.api.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping(path="/",produces=MediaType.APPLICATION_JSON_VALUE)
	public String home() {
		return "{\"success\":\"Seja Bem vindo ao Boleto-API\"}";
	}
	
}
