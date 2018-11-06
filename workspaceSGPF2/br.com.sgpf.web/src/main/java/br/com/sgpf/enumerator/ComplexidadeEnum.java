package br.com.sgpf.enumerator;

public enum ComplexidadeEnum {
	
	BAIXA("Baixa"),
	MEDIA("Média"), 
	ALTA("Alta");
	
	private String descricao;
	
	private ComplexidadeEnum(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;	
	}
	
}
