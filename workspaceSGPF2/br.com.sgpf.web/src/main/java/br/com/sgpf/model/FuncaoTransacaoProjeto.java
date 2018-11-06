package br.com.sgpf.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.sgpf.enumerator.ComplexidadeEnum;
import br.com.sgpf.enumerator.ElementoContagemEnum;
import br.com.sgpf.enumerator.SimNaoEnum;

@Entity
@Table(name="FuncaoTransacaoProjeto")
public class FuncaoTransacaoProjeto {
		
	public FuncaoTransacaoProjeto(FuncaoTransacaoProjeto ftp){
		this.nome = ftp.getNome();
		this.descricao = ftp.getDescricao();
		this.tipoManutencao = ftp.getTipoManutencao();
		this.descricaoManutencao  = ftp.getDescricaoManutencao();
		this.elementoContagem = ftp.getElementoContagem();
		this.tiposDeDados = ftp.getTiposDeDados();
		this.projeto = ftp.getProjeto();
		this.perteceBaseline = SimNaoEnum.NAO;
		this.tiposDeDados = ftp.getTiposDeDados().stream().filter(f -> f.isAdicionadoManualmente()).collect(Collectors.toList());
	}
	
	public FuncaoTransacaoProjeto(){
		tiposDeDados = new ArrayList<>();
	}
	
	@Id @GeneratedValue
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private char tipoManutencao; // I- Inclusão, A - Alteração, E - Exclusão
	
	private String descricaoManutencao;
	
	private ElementoContagemEnum elementoContagem; //3 - EE, 4 - CE, 5 - SE
	
	private boolean conversaoDados;
	
//	@OneToMany (mappedBy="funcaoTransacao", orphanRemoval=true, cascade=CascadeType.ALL)       
//	private List<FuncaoDadosProjeto> arquivosReferenciados;
		
	
	@ManyToMany (cascade={CascadeType.REMOVE,CascadeType.MERGE}, fetch=FetchType.EAGER)
	   @JoinTable(name="FuncaoTransacaoProjeto_Dado", joinColumns=
	   {@JoinColumn(name="id")}, inverseJoinColumns=
	     {@JoinColumn(name="id_dado")})
	private List<Dado> tiposDeDados;
	
	
	@ManyToMany (cascade={CascadeType.REMOVE,CascadeType.MERGE}, fetch=FetchType.EAGER)
	   @JoinTable(name="FuncaoTransacaoProjeto_FuncaoDadoProjeto", joinColumns=
	   {@JoinColumn(name="id")}, inverseJoinColumns=
	     {@JoinColumn(name="id_FuncaoDado")})
	private Set<FuncaoDadosProjeto> arquivosReferenciados;
	
	@ManyToOne
	private Projeto projeto;
	
	@Enumerated(EnumType.ORDINAL)
	private SimNaoEnum perteceBaseline;
	
	//private List<OutroTipoDado> outrosTiposDeDados;	

	
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

//	public List<FuncaoDadosProjeto> getArquivosReferenciados() {
//		return arquivosReferenciados;
//	}
//
//	public void setArquivosReferenciados(List<FuncaoDadosProjeto> arquivosReferenciados) {
//		this.arquivosReferenciados = arquivosReferenciados;
//	}

	public void adicionarTipoDeDados(List<Dado> dados){
		List<Long> idsJaCadastrados = tiposDeDados.stream().filter(t -> !t.isAdicionadoManualmente())
				.mapToLong(s -> s.getId()).boxed().collect(Collectors.toList());
		
		dados.forEach(d -> {
			if(!idsJaCadastrados.contains(d.getId()))
				tiposDeDados.add(d);
		});
	}
	
	public List<Dado> getTiposDeDados() {
		return tiposDeDados;
	}

	public void setTiposDeDados(List<Dado> tiposDeDados) {
		this.tiposDeDados = tiposDeDados;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public void setTipoManutencao(char tipoManutencao) {
		this.tipoManutencao = tipoManutencao;
	}

	public char getTipoManutencao() {
		return tipoManutencao;
	}

	public SimNaoEnum getPerteceBaseline() {
		return perteceBaseline;
	}

	public void setPerteceBaseline(SimNaoEnum perteceBaseline) {
		this.perteceBaseline = perteceBaseline;
	}

	public boolean isConversaoDados() {
		return conversaoDados;
	}

	public void setConversaoDados(boolean conversaoDados) {
		this.conversaoDados = conversaoDados;
	}

	public ComplexidadeEnum getComplexidade(){
		return recuperarComplexidade();
	}
	
	public ComplexidadeEnum recuperarComplexidade(){

					
		int quantidadeTR = arquivosReferenciados.size();					
		int quantidadeTD = tiposDeDados.size(); 
		
		if(elementoContagem.equals(ElementoContagemEnum.EE)){
			if(quantidadeTR == 1){
				if(quantidadeTD < 16)
					return ComplexidadeEnum.BAIXA;
				else
					return ComplexidadeEnum.MEDIA;
			}
			if(quantidadeTR == 2){
				if(quantidadeTD < 5)
					return ComplexidadeEnum.BAIXA;
				if(quantidadeTD < 16)
					return ComplexidadeEnum.MEDIA;
				else
					return ComplexidadeEnum.ALTA;
			}
			
			else{
				if(quantidadeTD < 5)
					return ComplexidadeEnum.MEDIA;
				else
					return ComplexidadeEnum.ALTA;
			}
			
		}
	
		if(elementoContagem.equals(ElementoContagemEnum.CE)){
			if(quantidadeTR == 1){
				if(quantidadeTD < 20)
					return ComplexidadeEnum.BAIXA;
				else
					return ComplexidadeEnum.MEDIA;
			}
			
			if(quantidadeTR  <= 3){
				if(quantidadeTD < 6)
					return ComplexidadeEnum.BAIXA;
				if(quantidadeTD < 20)
					return ComplexidadeEnum.MEDIA;
				else
					return ComplexidadeEnum.ALTA;
			}
			
			else{
				if(quantidadeTD < 6)
					return ComplexidadeEnum.MEDIA;				
				else
					return ComplexidadeEnum.ALTA;
			}
			
		}
		
		
		else{
			if(quantidadeTR == 1){
				if(quantidadeTD < 20)
					return ComplexidadeEnum.BAIXA;
				else
					return ComplexidadeEnum.MEDIA;
			}
			
			if(quantidadeTR  <= 3){
				if(quantidadeTD < 6)
					return ComplexidadeEnum.BAIXA;
				if(quantidadeTD < 20)
					return ComplexidadeEnum.MEDIA;
				else
					return ComplexidadeEnum.ALTA;
			}
			else{
				if(quantidadeTD < 6)
					return ComplexidadeEnum.MEDIA;				
				else
					return ComplexidadeEnum.ALTA;
			}
		}
			
					
	}

	
	public int getTamanhoPF(){
		ComplexidadeEnum complexidade = recuperarComplexidade();
		
		if (elementoContagem.equals(ElementoContagemEnum.EE)) 
		{
			if (complexidade.equals(ComplexidadeEnum.BAIXA))
				return 3;
			if (complexidade.equals(ComplexidadeEnum.MEDIA))
				return 4;
			else
				return 6;
		}
		else if (elementoContagem.equals(ElementoContagemEnum.SE)) 
		{
			if (complexidade.equals(ComplexidadeEnum.BAIXA))
				return 4;
			if (complexidade.equals(ComplexidadeEnum.MEDIA))
				return 5;
			else
				return 7;
		}
		else
		{
			if (complexidade.equals(ComplexidadeEnum.BAIXA))
				return 3;
			if (complexidade.equals(ComplexidadeEnum.MEDIA))
				return 4;
			else
				return 6;
		}
	}

	public Set<FuncaoDadosProjeto> getArquivosReferenciados() {
		return arquivosReferenciados;
	}

	public void setArquivosReferenciados(Set<FuncaoDadosProjeto> arquivosReferenciados) {
		this.arquivosReferenciados = arquivosReferenciados;
	}

	
	
	
	
}
