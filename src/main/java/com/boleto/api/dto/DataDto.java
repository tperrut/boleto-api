package com.boleto.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class DataDto {
	
	@NotNull(message="Data Pagamneto não pode ser vazia")
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	private LocalDate dataPagamento;
	

}
