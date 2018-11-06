package br.com.sgpf.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.sgpf.enumerator.ComplexidadeEnum;
import br.com.sgpf.enumerator.ElementoContagemEnum;
import br.com.sgpf.enumerator.SimNaoEnum;

@Entity
@Table(name="FuncaoDadosProjeto")
public class FuncaoDadosProjeto implements EntityBase{

	public FuncaoDadosProjeto(){}
	
	public FuncaoDadosProjeto(FuncaoDadosProjeto funcao){
		this.descricao = funcao.getDescricao();
		this.descricaoManutencao = funcao.getDescricaoManutencao();
		this.elementoContagem = funcao.getElementoContagem();
		this.nome = funcao.getNome();
		this.perteceBaseline = SimNaoEnum.NAO;
		this.projeto = funcao.getProjeto();
		this.tipoManutencao = funcao.getTipoManutencao();
		if(funcao.getTiposDeRegistros() != null){
			this.tiposDeRegistros = new ArrayList<>();
			funcao.getTiposDeRegistros().forEach(t -> this.tiposDeRegistros.add(new TipoRegistro(t)));
		}
					 
	}
	
	@Id @GeneratedValue
	private Long id;
	
	private String nome;
	private String descricao;
	private char tipoManutencao; // I- Inclusão, A - Alteração, E - Exclusão
	
	private String descricaoManutencao;
	
	@Enumerated(EnumType.ORDINAL)
	private ElementoContagemEnum elementoContagem;
	
	@Enumerated(EnumType.ORDINAL)
	private SimNaoEnum perteceBaseline;
		
	@OneToMany (mappedBy="funcaoDadosProjeto", orphanRemoval=true, cascade=CascadeType.ALL)       
	private List<TipoRegistro> tiposDeRegistros;
	
	@ManyToOne(fetch=FetchType.EAGER)	
	private Projeto projeto;
	
	@ManyToOne
	private FuncaoTransacaoProjeto funcaoTransacao;
	
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public char getTipoManutencao() {
		return tipoManutencao;
	}

	public void setTipoManutencao(char tipoManutencao) {
		this.tipoManutencao = tipoManutencao;
	}
	
	public String getDescricaoTipoManutencao(){
		switch (tipoManutencao) {
		case 'I':
			return "INCLUSÃO";
		case 'A':
			return "ALTERAÇÃO";
		case 'E':
			return "EXCLUSÃO";					
		}
		
		return "-";
	}

	public String getDescricaoManutencao() {
		return descricaoManutencao;
	}

	public void setDescricaoManutencao(String descricaoManutencao) {
		this.descricaoManutencao = descricaoManutencao;
	}
	
	public ElementoContagemEnum getElementoContagem() {
		return elementoContagem;
	}

	public void setElementoContagem(ElementoContagemEnum elementoContagem) {
		this.elementoContagem = elementoContagem;
	}

	public Projeto getProjeto() {
		return this.projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<TipoRegistro> getTiposDeRegistros() {
		return tiposDeRegistros;
	}

	public void setTiposDeRegistros(List<TipoRegistro> tiposDeRegistros) {
		this.tiposDeRegistros = tiposDeRegistros;
	}		

	public SimNaoEnum getPerteceBaseline() {
		return perteceBaseline;
	}

	public void setPerteceBaseline(SimNaoEnum perteceBaseline) {
		this.perteceBaseline = perteceBaseline;
	}
	
	public ComplexidadeEnum getComplexidade(){
		return recuperarComplexidade();
	}
		
	public ComplexidadeEnum recuperarComplexidade(){
		int quantidadeTR = tiposDeRegistros.size();					
		int quantidadeTD = 0; 
		
		for(TipoRegistro tr:tiposDeRegistros)
			quantidadeTD += tr.getDados().size();
				
		
		if(quantidadeTR == 1)
			if(quantidadeTD < 51)
				return ComplexidadeEnum.BAIXA;
			else
				return ComplexidadeEnum.MEDIA;
			
		if (quantidadeTR >= 2 && quantidadeTR<=5){
			
			if(quantidadeTD < 20)
				return ComplexidadeEnum.BAIXA;
		    if (quantidadeTD >=20 && quantidadeTD <51)
		    	return ComplexidadeEnum.MEDIA;
		    else
		    	return ComplexidadeEnum.ALTA;
		}
		else{
			if (quantidadeTD < 20)
				return ComplexidadeEnum.MEDIA;			
			else
				return ComplexidadeEnum.MEDIA;
			
		}			
	}

	
	public int getTamanhoPF(){
		ComplexidadeEnum complexidade = recuperarComplexidade();
		
		if (this.elementoContagem.getChave()  == ElementoContagemEnum.ALI.getChave()) 
		{
			if (complexidade.equals(ComplexidadeEnum.BAIXA))
				return 7;
			if (complexidade.equals(ComplexidadeEnum.MEDIA))
				return 10;
			else
				return 15;
		}
		else
		{
			if (complexidade.equals(ComplexidadeEnum.BAIXA))
				return 5;
			if (complexidade.equals(ComplexidadeEnum.MEDIA))
				return 7;
			else
				return 10;
		}
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
	
		
	
}
