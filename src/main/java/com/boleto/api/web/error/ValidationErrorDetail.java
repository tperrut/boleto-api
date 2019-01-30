package com.boleto.api.web.error;

import java.util.Date;

import javax.annotation.Generated;

import com.boleto.api.web.error.ResourceNotFoundDetails.Builder;

public class ValidationErrorDetail extends ErrorDetail{
	private String field;
	private String fieldMessages;

	private ValidationErrorDetail(Builder builder) {
		this.titulo = builder.titulo;
		this.detalhe = builder.detalhe;
		this.statusCode = builder.statusCode;
		this.timestamp = builder.timestamp;
		this.developerMessage = builder.developerMessage;
		this.field = builder.field;
		this.fieldMessages = builder.fieldMessages;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getFieldMessages() {
		return fieldMessages;
	}
	public void setFieldMessages(String fieldMessages) {
		this.fieldMessages = fieldMessages;
	}
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String titulo;
		private String detalhe;
		private int statusCode;
		private Date timestamp;
		private String developerMessage;
		private String field;
		private String fieldMessages;


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

		public Builder timestamp(Date timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder developerMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}

		
		public Builder field(String field) {
			this.field = field;
			return this;
		}

		public Builder fieldMessages(String fieldMessages) {
			this.fieldMessages = fieldMessages;
			return this;
		}

		public ValidationErrorDetail build() {
			return new ValidationErrorDetail(this);
		}
	}


		
		

			
}
