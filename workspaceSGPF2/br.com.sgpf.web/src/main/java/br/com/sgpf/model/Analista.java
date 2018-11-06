package br.com.sgpf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Analista")
public class Analista implements EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8094244933095027309L;

	@Id @GeneratedValue
	private Long id;
	
	private String nome;
	private String email;
	private Integer telefone;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getTelefone() {
		return telefone;
	}
	public void setTelefone(Integer telefone) {
		this.telefone = telefone;
	}
	
	
	public boolean equals(Object obj)
	{
		if (this == obj)//mesma instancia
			return true;
		if (obj == null)//objeto null sempre igual a false
			return false;
		if (getClass() != obj.getClass())//são de de classes diferentes
			return false;
		Analista analista = (Analista)obj;
		if (this.id == analista.id){
			return true;
		}
		else
		{
			return false;
		}
		
	}
		
}
	
	

