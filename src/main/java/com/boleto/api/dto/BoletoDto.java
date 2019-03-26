package com.boleto.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.boleto.api.model.Boleto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class BoletoDto {
	
	@NotNull(message = "Total não pode ser vazio")
	private BigDecimal total;
	
	@NotEmpty(message = "Cliente não pode ser vazio")
	private String cliente;
	
	@NotNull(message="Data Vencimento não pode ser vazio")
	@DateTimeFormat(iso = ISO.TIME, pattern="yyyy-MM-dd")
	private LocalDate dataVencimento;
	
	public BoletoDto(String cli, LocalDate dtVencimento, BigDecimal totalParam) {
		this.total=totalParam;
		this.dataVencimento= dtVencimento;
		this.cliente= cli;
	}

	public Boleto converteBoletoDtoToBoleto() {
		return new Boleto(this.getCliente(),this.getDataVencimento(),this.getTotal());
	}
}
