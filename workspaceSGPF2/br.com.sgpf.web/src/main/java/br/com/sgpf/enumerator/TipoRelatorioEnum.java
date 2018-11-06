package br.com.sgpf.enumerator;

public enum TipoRelatorioEnum {
	
	POR_PROJETO("RP","Relatório por Projeto"),
	POR_SISTEMA("RS","Relatório por Sistema");
	
	private String codigo;
	private String descricao;
	
	private TipoRelatorioEnum(String codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	
}
