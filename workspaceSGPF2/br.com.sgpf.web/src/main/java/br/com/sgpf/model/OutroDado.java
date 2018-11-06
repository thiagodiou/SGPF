package br.com.sgpf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="OutroDado")
public class OutroDado {

	@Id @GeneratedValue
	private Long id;
	
	private String nome;
	
}
