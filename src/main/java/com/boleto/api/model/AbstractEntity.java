package com.boleto.api.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@SuppressWarnings("serial")
@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

	@Id @GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2", strategy = "uuid2")
	private String id;
		
}

