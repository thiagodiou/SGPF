package br.com.sgpf.enumerator;

public enum ElementoContagemEnum {
	
	ALI(1, "ALI"),
	AIE(2, "AIE"),
	EE(3, "EE"),
	CE(4, "CE"),
	SE(5, "SE");
	
	private String descricao;
	
	private int chave;
	
	private ElementoContagemEnum(int chave, String descricao){
		this.chave = chave;
		this.descricao = descricao;
	}

	
	public static ElementoContagemEnum encontrarElementoPorChave(int chave){
		switch (chave) {
		case 1:
			return ALI;
		case 2:
			return AIE;			
		case 3:
			return EE;
		case 4:
			return CE;
		case 5:
			return SE;		
		}
		
		return null;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public int getChave() {
		return chave;
	}
	
	
	
	
	
}
