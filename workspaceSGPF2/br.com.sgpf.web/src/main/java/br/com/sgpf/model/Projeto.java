package br.com.sgpf.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.sgpf.enumerator.SimNaoEnum;

@Entity
@Table(name="Projeto")
public class Projeto implements EntityBase{


	private static final long serialVersionUID = -3013816726255850215L;

	
	public Projeto(){
		funcoesDados = new ArrayList<FuncaoDadosProjeto>();
	}
	
	@Id @GeneratedValue
	private Long id;
	
	
	private String identificador;
	private String titulo;
	private String descricao;
	private int tipoProjeto; // 1 - Projeto Desenvolvimento, 2 - Projeto de Melhoria e 3 - Aplicação
	private boolean concluido = false;
	
	@ManyToOne	
	private Sistema sistema;
	
	@ManyToOne
	private Analista analista;
	
	
	@OneToMany(mappedBy="projeto", cascade={CascadeType.ALL})
	private List<FuncaoDadosProjeto> funcoesDados;
	
	@OneToMany(mappedBy="projeto",  cascade={CascadeType.ALL})
	private List<FuncaoTransacaoProjeto> funcoesTransacao;

	
	public void concluir(){
		boolean semFuncaoDados = getFuncoesDados() == null || getFuncoesDados().isEmpty();
		boolean semFuncaoTransacao  = getFuncoesTransacao() == null || getFuncoesTransacao().isEmpty();
		
		if(semFuncaoDados)
			throw new RuntimeException("Projeto sem função de dados");
		
		if(semFuncaoTransacao)
			throw new RuntimeException("Projeto sem função de transação");
		
		getFuncoesDados().forEach(f -> f.setPerteceBaseline(SimNaoEnum.SIM));
		getFuncoesTransacao().stream().filter(f -> !f.isConversaoDados()).forEach(f -> f.setPerteceBaseline(SimNaoEnum.SIM));
		setConcluido(true);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getTipoProjeto() {
		return tipoProjeto;
	}

	public void setTipoProjeto(int tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}

	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	public void setAnalista(Analista analista){
		this.analista = analista;
	}
	
	public Analista getAnalista(){
		return this.analista;
	}
	
	public void setSistema(Sistema sistema){
		this.sistema = sistema;
	}
	
	public Sistema getSistema(){
		return this.sistema;
	}
	
	public List<FuncaoDadosProjeto> getFuncoesDados() {
		return funcoesDados;
	}

	public void setFuncoesDados(List<FuncaoDadosProjeto> funcoesDados) {
		this.funcoesDados = funcoesDados;
	}

	public List<FuncaoTransacaoProjeto> getFuncoesTransacao() {
		return funcoesTransacao;
	}

	public void setFuncoesTransacao(List<FuncaoTransacaoProjeto> funcoesTransacao) {
		this.funcoesTransacao = funcoesTransacao;
	}
	
	
	public int getCalcularTotalPF(){
		return calcularTotalPF(); 
	}
	
	public int calcularTotalPF()
	{
		int totalPFFuncaoDados = 0;
		int totalPFFuncaoTransacao = 0;
		
		
		   // para cada funcao de dados  do projeto ---> acumula valor da funcao em PF
		for (int i=0;i<this.funcoesDados.size();i++){
			totalPFFuncaoDados += this.funcoesDados.get(i).getTamanhoPF();
		}
		  // para cada funcao de transacao  do projeto ---> acumula valor da funcao em PF
		//totalPFFuncaoDados += this.funcoesTransacao.getTamanhoPF(); 
		
		//soma valor total do projeto
		return totalPFFuncaoDados + totalPFFuncaoTransacao;
		
	}
	
	
	
	public boolean equals(Object obj)
	{
		if (this == obj)//mesma instancia
			return true;
		if (obj == null)//objeto null sempre igual a false
			return false;
		if (getClass() != obj.getClass())//são de de classes diferentes
			return false;
		Projeto projeto = (Projeto)obj;
		if (this.id == projeto.id){
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
