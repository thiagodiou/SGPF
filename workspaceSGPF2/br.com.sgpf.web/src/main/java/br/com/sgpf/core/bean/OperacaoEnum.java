package br.com.sgpf.core.bean;

public enum OperacaoEnum {
	
	INCLUIR("INCLUIR"),
	ALTERAR("ALTERAR"),
	FILTRAR("FILTRAR");
	
	
	private String rotulo;
	
	private OperacaoEnum(String rotulo){
		this.rotulo = rotulo;	
	}
	
	public String getRotulo(){
		return rotulo;
	}
}
