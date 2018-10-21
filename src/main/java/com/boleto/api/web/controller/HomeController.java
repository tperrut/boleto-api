package com.boleto.api.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@RequestMapping(path="/",method=RequestMethod.GET ,produces= {"application/JSON"})
	public String home() {
		return "{\"success\":\"Seja Bem vindo ao Boleto-API\"}";
	}
	
}
