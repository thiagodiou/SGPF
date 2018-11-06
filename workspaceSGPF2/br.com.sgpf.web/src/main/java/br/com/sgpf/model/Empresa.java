package br.com.sgpf.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Empresa")
public class Empresa implements EntityBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2144607866012218725L;

	@Id @GeneratedValue
	private Long id;               // PK
	
	private String cnpj;
	private String razaoSocial;
	private String nomeFantasia;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="empresa")
	private List<Sistema> sistemas;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	
	public List<Sistema> getSistemas()
	{
		return this.sistemas;
	}
	
	public boolean equals(Object obj)
	{
		if (this == obj)//mesma instancia
			return true;
		if (obj == null)//objeto null sempre igual a false
			return false;
		if (getClass() != obj.getClass())//são de de classes diferentes
			return false;
		Empresa emp = (Empresa)obj;
		if (this.id == emp.id){
			return true;
		}
		else
		{
			return false;
		}
		
	}
}