package br.com.sgpf.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Sistema")
public class Sistema implements EntityBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8164619790542333043L;

	@Id @GeneratedValue
	private Long id;           // PK
	
	private String sigla;
	private String nome;
	private String descricao;	
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Empresa.class)
	private Empresa empresa;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setEmpresa(Empresa empresa)
	{
		this.empresa = empresa;
	}
	
	public Empresa getEmpresa()
	{
		return this.empresa;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)//mesma instancia
			return true;
		if (obj == null)//objeto null sempre igual a false
			return false;
		if (getClass() != obj.getClass())//são de de classes diferentes
			return false;
		Sistema sistema = (Sistema)obj;
		if (this.id == sistema.id){
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
}