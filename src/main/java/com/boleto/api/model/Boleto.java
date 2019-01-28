package com.boleto.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "BOLETO")
public class Boleto extends AbstractEntity{
	
	@NotNull(message = "total_in_cents can not be null")
	@Column(nullable = false)
	private BigDecimal total_in_cents;
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataVencimento;
	
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataPagamento;
	
	@Column(nullable = false)
	private String customer;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EnumStatus status;
	
	public BigDecimal getTotal_in_cents() {
		return total_in_cents;
	}
	public void setTotal_in_cents(BigDecimal total_in_cents) {
		this.total_in_cents = total_in_cents;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public EnumStatus getStatus() {
		return status;
	}
	public void setStatus(EnumStatus status) {
		this.status = status;
	}
	public LocalDate getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
}
