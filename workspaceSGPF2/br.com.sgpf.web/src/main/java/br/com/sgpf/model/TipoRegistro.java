package br.com.sgpf.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TipoRegistro")
public class TipoRegistro {
	
	public TipoRegistro(){}
	
	public TipoRegistro(TipoRegistro tipo){
		this.nome = tipo.getNome();
		this.funcaoDadosProjeto = tipo.getFuncaoDadosProjeto();
		if(tipo.getDados() != null){
			this.dados = new ArrayList<>();
			tipo.getDados().forEach(d -> dados.add(new Dado(d)));
		}
	}
	
	@Id @GeneratedValue 
	private Long id;
	
	private String nome;

	@OneToMany(fetch=FetchType.EAGER, mappedBy="tipoRegistro", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Dado> dados;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private FuncaoDadosProjeto funcaoDadosProjeto;

	public int getQuantidadeDados(){
		return dados == null ? 0 : dados.size();
	}
	
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

	public List<Dado> getDados() {
		return dados;
	}

	public void setDados(List<Dado> dados) {
		this.dados = dados;
	}
	
	
	@Override
	public boolean equals(Object obj){
		TipoRegistro outro = (TipoRegistro) obj;
		if(outro.getId() != null && outro.getId() != 0l){
			return outro.getId() == this.getId();
		}else{
			return outro.getNome().equals(this.getNome());
		}
	}

	public FuncaoDadosProjeto getFuncaoDadosProjeto() {
		return funcaoDadosProjeto;
	}

	public void setFuncaoDadosProjeto(FuncaoDadosProjeto funcaoDadosProjeto) {
		this.funcaoDadosProjeto = funcaoDadosProjeto;
	}
	
	
	
}
