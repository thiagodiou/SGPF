package br.com.sgpf.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Dado")
public class Dado {
	
	public Dado(){}
	
	public Dado (Dado dado){
		this.nome = dado.getNome();
		this.formato = dado.getFormato();
		this.tamanho = dado.getTamanho();
		this.validade = dado.getValidade();
	}
	
	@Id @GeneratedValue
	private Long id;
	
	private String nome;
	
	private String formato; 
	
	private double tamanho;
	
	private String validade;
	
	private boolean dadoDerivado;
	
	private boolean adicionadoManualmente;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private TipoRegistro tipoRegistro;
	
	@ManyToMany(mappedBy="tiposDeDados", fetch=FetchType.EAGER)
	private List<FuncaoTransacaoProjeto> funcaoTransacao;
	
	@Transient
	private boolean selecionado;
	
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

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public double getTamanho() {
		return tamanho;
	}

	public void setTamanho(double tamanho) {
		this.tamanho = tamanho;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}
	
	@Override
	public boolean equals(Object obj){
		Dado outro = (Dado) obj;
		if(outro.getId() != null && outro.getId() != 0l){
			return outro.getId() == this.getId();
		}else{
			return outro.getNome().equals(getNome()) &&  (outro.getFormato() == null || outro.getFormato() !=null && outro.getFormato().equals(getFormato()));
		}
	}

	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public List<FuncaoTransacaoProjeto> getFuncaoTransacao() {
		return funcaoTransacao;
	}

	public void setFuncaoTransacao(List<FuncaoTransacaoProjeto> funcaoTransacao) {
		this.funcaoTransacao = funcaoTransacao;
	}

	public boolean isDadoDerivado() {
		return dadoDerivado;
	}

	public void setDadoDerivado(boolean dadoDerivado) {
		this.dadoDerivado = dadoDerivado;
	}

	public boolean isAdicionadoManualmente() {
		return adicionadoManualmente;
	}

	public void setAdicionadoManualmente(boolean adicionadoManualmente) {
		this.adicionadoManualmente = adicionadoManualmente;
	}

	
	
	
	
	
}
