package com.boleto.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.boleto.api.model.Boleto;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter 
public class BoletoDto {
	
	@NotNull(message = "Total não pode ser vazio")
	private BigDecimal total;
	
	@NotEmpty(message = "Cliente não pode ser vazio")
	private String cliente;
	
	@NotNull(message="Data Vencimento não pode ser vazio")
	private LocalDate dataVencimento;
	
	public BoletoDto(String cli, LocalDate dtVencimento, BigDecimal totalParam) {
		this.total=totalParam;
		this.dataVencimento= dtVencimento;
		this.cliente= cli;
	}

	public Boleto converteBoletoDtoToBoleto(BoletoDto dto) {
		return new Boleto(dto.getCliente(),dto.getDataVencimento(),dto.getTotal());
	}
}
