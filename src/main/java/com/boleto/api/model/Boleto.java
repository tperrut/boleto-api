package com.boleto.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.boleto.api.dto.BoletoDetalheDto;
import com.boleto.api.dto.BoletoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "BOLETO")
@Getter @Setter @NoArgsConstructor
public class Boleto extends AbstractEntity{
	
	@Column(nullable = false)
	private BigDecimal total;
	
	@Transient
	private Double multa;
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataVencimento;
	
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataPagamento;
	
	@NotEmpty(message = "Cliente não pode ser vazio")
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
	
	@PrePersist
	public void onPrePersist() { System.out.println("Tá rolando um PrePersist");}
	       
	@PreUpdate
	public void onPreUpdate() { System.out.println("Tá rolando um PreUpdate");}
	
	public BoletoDto converteBoletoToDto() {
		return new BoletoDto(this.getCliente(),this.getDataVencimento(),this.getTotal());
	}
	
	public BoletoDetalheDto converteBoletoToDetalheDto() {
		return BoletoDetalheDto.builder().
				multa(this.getMulta()).
				total(this.getTotal()).
				status(this.getStatus()).
				id(this.getId()).
				cliente(this.getCliente()).
				dataVencimento(this.getDataVencimento()).
				build();
	}
	
	/**
	 * Regra para definir se vamos calcular o boleto:
	 * 
	 * Não pode ter os seguintes Status: PAID e CANCELED |
	 * Deve estar atrasado
	 * 
	 *
	 * @return boolean
	 */
	public boolean isCalculable() {
		return this.isAtrasado()  & isPending()  ;
	}
	
	
	@JsonIgnore
	public boolean isAtrasado() {
		return LocalDate.now().isAfter(this.getDataVencimento());
	}
	
	@JsonIgnore
	public boolean isPaid() {
		return this.getStatus().equals(EnumStatus.PAID);
	}
	
	@JsonIgnore
	public boolean isPending() {
		return this.getStatus().equals(EnumStatus.PENDING);
	}
	
	@JsonIgnore
	public boolean isCanceled() {
		return this.getStatus().equals(EnumStatus.CANCELED);
	}
	
}
		
		
