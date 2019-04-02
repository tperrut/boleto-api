package com.boleto.api.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boleto.api.util.ConstanteUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;

@RestController
public class HomeController {
	
	@Value(value="${spring.server.url}")
	String server;
	
	@GetMapping(path="/",produces=MediaType.APPLICATION_JSON_VALUE)
	public Object home() {
		Map<String,String> map= new HashMap<String,String>();
		map.put("msg", ConstanteUtil.WELCOME);
		map.put("doc", this.server+ConstanteUtil.SWAGGER);
		JSONPObject response = new JSONPObject("",map);
		return response.getValue();
	}
	
}
