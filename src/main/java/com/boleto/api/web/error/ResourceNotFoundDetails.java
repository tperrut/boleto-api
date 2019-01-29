package com.boleto.api.web.error;

import javax.annotation.Generated;

public class ResourceNotFoundDetails {
	private String titulo;
	private String detalhe;
	private int statusCode;
	private long timestamp;
	private String developerMessage;

	@Generated("SparkTools")
	private ResourceNotFoundDetails(Builder builder) {
		this.titulo = builder.titulo;
		this.detalhe = builder.detalhe;
		this.statusCode = builder.statusCode;
		this.timestamp = builder.timestamp;
		this.developerMessage = builder.developerMessage;
	}
	
	public ResourceNotFoundDetails() {
		// TODO Auto-generated constructor stub
	}

	public String getTitulo() {
		return titulo;
	}
	public String getDetalhe() {
		return detalhe;
	}
	public int getStatusCode() {
		return statusCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Creates builder to build {@link ResourceNotFoundDetails}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link ResourceNotFoundDetails}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private String titulo;
		private String detalhe;
		private int statusCode;
		private long timestamp;
		private String developerMessage;

		private Builder() {
		}

		public Builder titulo(String titulo) {
			this.titulo = titulo;
			return this;
		}

		public Builder detalhe(String detalhe) {
			this.detalhe = detalhe;
			return this;
		}

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder timestamp(long timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder developerMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}

		public ResourceNotFoundDetails build() {
			return new ResourceNotFoundDetails(this);
		}
	}

	
	
}
