package com.boleto.api.model;

public enum EnumStatus {
	
	PENDING(1), PAID(2), CANCELED(3);
	
	private int id;

	EnumStatus(int id) {
		this.id = id;
	}
	
	public int id() {
		return this.id;
	}
}
