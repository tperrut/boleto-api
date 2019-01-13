package com.boleto.api.web.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;
import com.boleto.api.service.BoletoService;


@RestController
@RequestMapping("/rest")
public class BoletoController {
	
	@Autowired
	private BoletoService service;
	
	@GetMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarBoletos(){ 
		return new ResponseEntity(service.buscarTodos(), HttpStatus.OK) ;
	}
	 
	@PostMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarBoleto(@RequestBody Boleto ticket){ 
		Boleto salvo = null;
		//TODO tratar erro corpo vazio status 400
		ticket.setStatus(EnumStatus.PENDING);
		try {
			salvo = service.salvar(ticket);
		} catch(DataIntegrityViolationException ex){
			ex.printStackTrace();
			return new ResponseEntity("Erro ao Salvar", HttpStatus.UNPROCESSABLE_ENTITY) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity(salvo, HttpStatus.CREATED) ;
	} 
	
	/**
	 *   --Pagar um boleto---
	 *  Esse método da API deve alterar o status do boleto para PAID.
	 * 
	 * @param paynent_date 
	 * {@code Request:	{ "payment_date" : "2018-06-30" }	 }		
	 * 
	 * @param id
	 * @return 204 No Content.
	 */
	@PutMapping("/boleto/{id}/pagamento")
	public ResponseEntity<Object> pagarboleto(@RequestBody Boleto boleto, @PathVariable String id) {

		Optional<Boleto> boletoOptional = service.buscarPorId(id);

		if (!boletoOptional.isPresent())
			return ResponseEntity.notFound().build();
		
		boletoOptional.get().setDataPagamento(boleto.getDataPagamento());
		boletoOptional.get().setStatus(EnumStatus.PAID);
		service.salvar(boletoOptional.get());

		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/boleto/{id}")
	public ResponseEntity<Object> cancelarBoleto(@PathVariable String id) {

		Optional<Boleto> boletoOptional = service.buscarPorId(id);

		if (!boletoOptional.isPresent())
			return ResponseEntity.notFound().build();
		
		boletoOptional.get().setStatus(EnumStatus.CANCELED);
		service.salvar(boletoOptional.get());

		return ResponseEntity.noContent().build();
	}
	
}
