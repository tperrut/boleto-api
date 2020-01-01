package com.boleto.api.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.boleto.api.util.ConstanteUtil;
import com.boleto.api.web.exception.BusinessException;
import com.boleto.api.web.exception.InternalServerException;
import com.boleto.api.web.exception.ResourceNotFoundException;
import com.boleto.api.web.response.ResponseApi;


@RestController
@RequestMapping("/rest")
public class BoletoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BoletoService service;
	
	@GetMapping(path="/boletos",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> listarBoletos(){ 
		LOGGER.info(ConstanteUtil.LISTAR_BOLETOS);
		ResponseApi<BoletoDto> boletoResponse;
		
		List<Boleto>  boletos = null;
		try {
			boletos = service.findAll();
			List<BoletoDto> dtos = convertListBoletoToDto(boletos);
			boletoResponse = new ResponseApi<BoletoDto>();
			boletoResponse.setData(dtos);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + ConstanteUtil.ERRO_LISTAR_BOLETO);
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + ConstanteUtil.ERRO_LISTAR_BOLETO,e);
		}
		
		if(boletos.isEmpty()) return new ResponseEntity<>(boletoResponse, HttpStatus.NO_CONTENT) ;		
		
		return new ResponseEntity<>(boletos, HttpStatus.OK) ;
	}
	
	
	@GetMapping(path="/boletos/pagedAndSorted",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarBoletosPaged(@PageableDefault(size = 3) Pageable page){ 
		LOGGER.info(ConstanteUtil.LISTAR_BOLETOS_PAGED);
		ResponseApi<Page<Boleto>> boletoResponse = new ResponseApi<Page<Boleto>>();
		Page<Boleto>  boletos = null;
		try {
			boletos = service.findAll(page);
			boletoResponse.setDataPaged(boletos);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + ConstanteUtil.LISTAR_BOLETOS_PAGED);
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + ConstanteUtil.ERRO_LISTAR_BOLETO,e);
		}
		
		if(!boletoResponse.getDataPaged().hasContent()) return new ResponseEntity<>(boletoResponse, HttpStatus.NO_CONTENT) ;		
		
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
	@GetMapping(path="/boletos/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> detalharBoleto(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){ 
		LOGGER.info(ConstanteUtil.DETALHAR_BOLETO+id);
		LOGGER.info(ConstanteUtil.USER_DETAIL_NOME+ userDetails.getUsername());
		verificarSeBoletoExiste(id);
		Optional<Boleto> boleto = service.buscarPorId(id);
		Boleto resposta = boleto.get();
		
		if(resposta.isCalculable() ) {
			resposta = service.calcularMulta(boleto.get());
		}
		
		ResponseApi<BoletoDetalheDto> boletoResponse = new ResponseApi<BoletoDetalheDto>();
		boletoResponse.setData(Arrays.asList(resposta.converteBoletoToDetalheDto()));
		return new ResponseEntity<>(boletoResponse , HttpStatus.OK) ;
	}
	
	@GetMapping(path="/boletos/cliente/{cliente}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseApi<BoletoDetalheDto>> findByName(@PathVariable String cliente){ 
		LOGGER.info(ConstanteUtil.FIND_BY_NAME + cliente );
		Optional<Boleto> boleto = null;
		Boleto resposta = null;
		verificarSeClienteExiste(cliente);
		try {
			boleto = service.buscarPorCliente(cliente);
			System.out.println(service.buscarPorCliente(cliente));
			resposta = boleto.get();
			if(resposta.isCalculable() )
				resposta = service.calcularMulta(boleto.get());
				
		} catch(Exception ex) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + ex.getMessage() + ConstanteUtil.FIND_BY_NAME + cliente);
			throw new InternalServerException(ConstanteUtil.ERRO_BOLETO_POR_NOME,ex);
		}
		ResponseApi<BoletoDetalheDto> boletoResponse = new ResponseApi<BoletoDetalheDto>();
		boletoResponse.setData(Arrays.asList(resposta.converteBoletoToDetalheDto()));
		return new ResponseEntity<>(boletoResponse , HttpStatus.OK) ;
	}

	

	@PostMapping(path="/boletos",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarBoleto(@RequestBody @Valid BoletoDto dto){ 
		LOGGER.info(ConstanteUtil.CRIAR_BOLETO+ dto.getCliente() );
		Boleto ticket = null;
		ResponseApi<BoletoDto> boletoResponse = new ResponseApi<BoletoDto>();
		try {
			ticket = (dto.converteBoletoDtoToBoleto());
			ticket.setStatus(EnumStatus.PENDING);
			
			ticket = service.salvar(ticket);
		} catch(Exception ex){
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + ex.getMessage() + ConstanteUtil.CRIAR_BOLETO+ dto.getCliente());
			throw new InternalServerException(ConstanteUtil.ERRO_CRIAR_BOLETO,ex);
		}
		
		boletoResponse.setData(Arrays.asList(ticket.converteBoletoToDto()));
		return new ResponseEntity<>(boletoResponse, HttpStatus.CREATED) ;
	}


	/**
	 *   --Pagar um boleto---
	 *  Esse método da API deve alterar o status do boleto para PAID.
	 * 
	 * @param dataPagamento
	 * {@code Request:	{ "payment_date" : "2018-06-30" }	 }		
	 * 
	 * @param id
	 * @return 204 No Content.
	 */
	@PutMapping("/boletos/{id}/pagamento")
	public ResponseEntity<Object> pagarboleto(@RequestBody @Valid DataDto dataPagamento, @PathVariable Long id) {
		LOGGER.info(ConstanteUtil.PAGAR_BOLETO + id );
		verificarSeBoletoExiste(id);
		
		Optional<Boleto> boletoOptional = service.buscarPorId(id);
		if(!boletoOptional.get().isPending()) {
			throw new BusinessException(ConstanteUtil.ERRO_NOT_PENDING);
		}	
		Boleto bol = null;
		
		if(boletoOptional.get().isCalculable() ) {
			bol = service.calcularMulta(boletoOptional.get());
		}
		
		try {
		boletoOptional.get().setDataPagamento(dataPagamento.getDataPagamento());
		boletoOptional.get().setStatus(EnumStatus.PAID);
		boletoOptional.get().setMulta(bol.getMulta());
		
		service.salvar(boletoOptional.get());
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + ConstanteUtil.PAGAR_BOLETO+ id);
			throw new InternalServerException(ConstanteUtil.ERRO_PAGAR,e);
		}
		ResponseApi<Boleto> boletoResponse = new ResponseApi<Boleto>();
		boletoResponse.setData(Arrays.asList(boletoOptional.get()));
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Regra para definir se é possivel CANCELAR um boleto:
	 * 
	 * NÂO pode ter os seguintes Status: PAID e CANCELED 
	 * deve ser apenas status PENDING
	 * 
	 * @param id resposta
	 * @return Boleto
	 */
	@DeleteMapping("/boletos/{id}")
	public ResponseEntity<Object> cancelarBoleto(@PathVariable Long id) {
		LOGGER.info(ConstanteUtil.CANCELAR_BOLETO + id );
		verificarSeBoletoExiste(id);
			
		Optional<Boleto> boleto = service.buscarPorId(id);
		if(!boleto.get().isPending()) 
			throw new BusinessException(ConstanteUtil.ERRO_NOT_PENDING);

		try {
			boleto.get().setStatus(EnumStatus.CANCELED);
			service.salvar(boleto.get());	
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR+ e.getMessage() + ConstanteUtil.CANCELAR_BOLETO+ id  );
			throw new InternalServerException(ConstanteUtil.ERRO_CANCELAR_BOLETO,e);
		}	
		return ResponseEntity.noContent().build();
	}
	
	public void verificarSeClienteExiste(String cliente) {
		Optional<Boleto> boletoOptional = service.buscarPorCliente(cliente);

		if (!boletoOptional.isPresent())
			throw new ResourceNotFoundException(ConstanteUtil.BOLETO_NOT_FOUND_BY_CLIENTE+ cliente);
	}



	public void verificarSeBoletoExiste(Long id) {
		Optional<Boleto> boletoOptional = service.buscarPorId(id);

		if (!boletoOptional.isPresent())
			throw new ResourceNotFoundException(ConstanteUtil.BOLETO_NOT_FOUND+ id);
	}
	
}
