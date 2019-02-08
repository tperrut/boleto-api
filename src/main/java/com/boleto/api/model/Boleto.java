package com.boleto.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.boleto.api.dto.BoletoDto;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "BOLETO")
@Getter @Setter
public class Boleto extends AbstractEntity{
	
	@Column(nullable = false)
	private BigDecimal total;
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataVencimento;
	
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataPagamento;
	
	@Column(nullable = false)
	private String cliente;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EnumStatus status;
	
	public Boleto(String clienteParam, LocalDate dtVencimento, BigDecimal totalParam) {
		this.cliente = clienteParam;
		this.dataVencimento = dtVencimento;
		this.total= totalParam;
	}
	
	public BoletoDto converteBoletoToDto(Boleto dto) {
		return new BoletoDto(dto.getCliente(),dto.getDataVencimento(),dto.getTotal());
	}
		
}
