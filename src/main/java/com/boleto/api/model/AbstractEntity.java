package com.boleto.api.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@SuppressWarnings("serial")
@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@Type(type="uuid-binary") private UUID refId;
	
	
	
}

