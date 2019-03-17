package com.boleto.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.boleto.api.model.EnumStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class BoletoDetalheDto {
	
	private Long id;
	private BigDecimal total;
	private String cliente;
	private LocalDate dataVencimento;
	private EnumStatus status;
	private Double multa;

	private BoletoDetalheDto(BuilderDetalheDto builder) {
		this.total = builder.total;
		this.cliente = builder.cliente;
		this.dataVencimento = builder.dataVencimento;
		this.status = builder.status;
		this.id = builder.id;
		this.multa = builder.multa;
	}

	
	public static BuilderDetalheDto builder() {
		return new BuilderDetalheDto();
	}

	public static final class BuilderDetalheDto {
		private BigDecimal total;
		private String cliente;
		private LocalDate dataVencimento;
		private EnumStatus status;
		private Long id;
		private Double multa;

		private BuilderDetalheDto() {
		}

		public BuilderDetalheDto total(BigDecimal total) {
			this.total = total;
			return this;
		}

		public BuilderDetalheDto cliente(String cliente) {
			this.cliente = cliente;
			return this;
		}

		public BuilderDetalheDto dataVencimento(LocalDate dataVencimento) {
			this.dataVencimento = dataVencimento;
			return this;
		}

		public BuilderDetalheDto status(EnumStatus status) {
			this.status = status;
			return this;
		}

		public BuilderDetalheDto id(Long id) {
			this.id = id;
			return this;
		}

		public BoletoDetalheDto build() {
			return new BoletoDetalheDto(this);
		}

		public BuilderDetalheDto multa(Double multa) {
			this.multa = multa;
			return this;
		}
	}
	
	
}
