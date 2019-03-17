package com.boleto.api.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.boleto.api.dto.BoletoDetalheDto;
import com.boleto.api.dto.BoletoDto;
import com.boleto.api.dto.DataDto;
import com.boleto.api.model.Boleto;
import com.boleto.api.model.EnumStatus;
import com.boleto.api.service.BoletoService;
import com.boleto.api.web.exception.BusinessException;
import com.boleto.api.web.exception.InternalServerException;
import com.boleto.api.web.exception.ResourceNotFoundException;
import com.boleto.api.web.response.ResponseApi;


@RestController
@RequestMapping("/rest")
public class BoletoController {
	
	@Autowired
	private BoletoService service;
	
	@Cacheable( "listarTodosCache" )
	@GetMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseApi<BoletoDto>> listarBoletos(){ 

		List<Boleto>  boletos = null;
		ResponseApi<BoletoDto> boletoResponse;
		
		try {
			boletos = service.buscarTodos();
			List<BoletoDto> dtos = convertListBoletoToDto(boletos);
			boletoResponse = new ResponseApi<BoletoDto>();
			boletoResponse.setData(dtos);
		} catch (Exception e) {
			throw new InternalServerException("Erro: "+ e.getMessage() + " ao listar boletos. Contate o admin!");
		}
		
		if(boletos.isEmpty()) return new ResponseEntity<>(boletoResponse, HttpStatus.NO_CONTENT) ;		
		
		return new ResponseEntity<>(boletoResponse, HttpStatus.OK) ;
	}
	
	
	
	private List<BoletoDto> convertListBoletoToDto(List<Boleto> boletos) {
		return boletos.stream().map(b -> b.converteBoletoToDto()).collect(Collectors.toList());
	}



	/**Esse método da API deve retornar um boleto filtrado pelo id, caso o boleto estiver atrasado deve
		ser calculado o valor da multa.
		Regra para o cálculo da multa aplicada por dia para os boletos atrasados:
			● Até 10 dias: Multa de 5% (Juros Simples)
			● Acima de 10 dias: Multa de 10% (Juros Simples)
	 * 
	 * @param id
	 * @return
	 */
	
	
	@GetMapping(path="/boleto/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> detalharBoleto(@PathVariable Long id){ 
		verificarSeBoletoExiste(id);
		Optional<Boleto> boleto = service.buscarPorId(id);
		Boleto resposta = boleto.get();
		
		if(isCalculable(resposta) ) {
			resposta = service.calcularMulta(boleto.get());
		}
		
		ResponseApi<BoletoDetalheDto> boletoResponse = new ResponseApi<BoletoDetalheDto>();
		boletoResponse.setData(Arrays.asList(resposta.converteBoletoToDetalheDto()));
		return new ResponseEntity<>(boletoResponse , HttpStatus.OK) ;
	}
	
	@GetMapping(path="/boleto/cliente/{cliente}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseApi<BoletoDetalheDto>> findByName(@PathVariable String cliente){ 

		Optional<Boleto> boleto = null;
		Boleto resposta = null;
		try {
			boleto = service.buscarPorCliente(cliente);
			resposta = boleto.get();
			
			if(isCalculable(resposta) ) {
				resposta = service.calcularMulta(boleto.get());
			}
		} catch(Exception ex) {
			throw new InternalServerException("Erro ao consultar boleto por nome. Contate o admin!");
		}
		ResponseApi<BoletoDetalheDto> boletoResponse = new ResponseApi<BoletoDetalheDto>();
		boletoResponse.setData(Arrays.asList(resposta.converteBoletoToDetalheDto()));
		return new ResponseEntity<>(boletoResponse , HttpStatus.OK) ;
	}


	/**
	 * Regra para definir se vamos calcular o boleto:
	 * 
	 * Não pode ter os seguintes Status: PAID e CANCELED |
	 * Deve estar atrasado
	 * 
	 * @param Boleto resposta
	 * @return Boleto
	 */
	private boolean isCalculable(Boleto resposta) {
		return !resposta.isPaid() || !resposta.isCanceled() || resposta.isAtrasado();
	}
	
	
	
	
	@PostMapping(path="/boleto",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarBoleto(@RequestBody @Valid BoletoDto dto){ 
		Boleto ticket = null;
		ResponseApi<BoletoDto> boletoResponse = new ResponseApi<BoletoDto>();
		try {
			ticket = (dto.converteBoletoDtoToBoleto(dto));
			ticket.setStatus(EnumStatus.PENDING);
			
			ticket = service.salvar(ticket);
		} catch(Exception ex){
			//TODO throw exception	return internalServerError(boletoResponse, ex);
			throw new InternalServerException("Erro ao Salvar o boleto. Contate o admin!");
		}
		
		boletoResponse.setData(Arrays.asList(ticket.converteBoletoToDto()));
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
	public ResponseEntity<Object> pagarboleto(@RequestBody @Valid DataDto dataPagamento, @PathVariable Long id) {
		verificarSeBoletoExiste(id);
		
		Optional<Boleto> boletoOptional = service.buscarPorId(id);
		if(!boletoOptional.get().isPending()) {
			throw new BusinessException("Boleto deve estar com o status PENDING para ser pago");
		}	
		

		boletoOptional.get().setDataPagamento(dataPagamento.getDataPagamento());
		boletoOptional.get().setStatus(EnumStatus.PAID);
		
		service.salvar(boletoOptional.get());
		
		ResponseApi<Boleto> boletoResponse = new ResponseApi<Boleto>();
		boletoResponse.setData(Arrays.asList(boletoOptional.get()));
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/boleto/{id}")
	public ResponseEntity<Object> cancelarBoleto(@PathVariable Long id) {
		verificarSeBoletoExiste(id);
		
		Optional<Boleto> boleto = service.buscarPorId(id);
		boleto.get().setStatus(EnumStatus.CANCELED);
		
		service.salvar(boleto.get());

		return ResponseEntity.noContent().build();
	}

	public void verificarSeBoletoExiste(Long id) {
		Optional<Boleto> boletoOptional = service.buscarPorId(id);

		if (!boletoOptional.isPresent())
			throw new ResourceNotFoundException("Boleto não encontrado para o ID: "+ id);
	}
	
}
