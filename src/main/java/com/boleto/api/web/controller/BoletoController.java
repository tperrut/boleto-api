package com.boleto.api.web.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

import com.boleto.api.dto.BoletoDto;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;
import com.boleto.api.service.BoletoService;
import com.boleto.api.web.exception.ResourceNotFoundException;
import com.boleto.api.web.response.ResponseApi;


@RestController
@RequestMapping("/rest")
public class BoletoController {
	
	@Autowired
	private BoletoService service;
	
	@Cacheable( "listarTodosCache" )
	@GetMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarBoletos(){ 
		List<Boleto> boletos = service.buscarTodos();
		if(boletos.isEmpty())
			new ResponseEntity<>(boletos, HttpStatus.NO_CONTENT) ;	
		
		return new ResponseEntity<>(boletos, HttpStatus.OK) ;
	}
	
	@GetMapping(path="/boleto/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> detalharBoleto(@PathVariable String id){ 
		verificarSeBoletoExiste(id);
		Optional<Boleto> boleto = service.buscarPorId(id);
		ResponseApi<Boleto> boletoResponse = new ResponseApi<Boleto>();
		boletoResponse.setData(boleto.get());
		return new ResponseEntity<>(boletoResponse , HttpStatus.OK) ;
	}
	
	@PostMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarBoleto(@RequestBody @Valid BoletoDto dto){ 
		Boleto ticket = null;
		ResponseApi<Boleto> boletoResponse = new ResponseApi<Boleto>();
		
		ticket = (dto.converteBoletoDtoToBoleto(dto));
		ticket.setStatus(EnumStatus.PENDING);
		
		try {
			ticket = service.salvar(ticket);
		} catch(Exception ex){
			ex.printStackTrace();
			return new ResponseEntity<>("Erro ao Salvar: ", HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
		
		boletoResponse.setData(ticket);
		return new ResponseEntity<>(boletoResponse, HttpStatus.CREATED) ;
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
	public ResponseEntity<Object> pagarboleto(@RequestBody LocalDate dataPagamento, @PathVariable String id) {
		verificarSeBoletoExiste(id);

		Optional<Boleto> boletoOptional = service.buscarPorId(id);
		boletoOptional.get().setDataPagamento(dataPagamento);
		boletoOptional.get().setStatus(EnumStatus.PAID);

		service.salvar(boletoOptional.get());
		ResponseApi<Boleto> boletoResponse = new ResponseApi<Boleto>();
		boletoResponse.setData(boletoOptional.get());
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/boleto/{id}")
	public ResponseEntity<Object> cancelarBoleto(@PathVariable String id) {
		verificarSeBoletoExiste(id);
		
		Optional<Boleto> boleto = service.buscarPorId(id);
		boleto.get().setStatus(EnumStatus.CANCELED);
		
		service.salvar(boleto.get());

		return ResponseEntity.noContent().build();
	}

	private void verificarSeBoletoExiste(String id) {
		Optional<Boleto> boletoOptional = service.buscarPorId(id);

		if (!boletoOptional.isPresent())
			throw new ResourceNotFoundException("Boleto não encontrado para o ID: "+ id);
	}
	
}
