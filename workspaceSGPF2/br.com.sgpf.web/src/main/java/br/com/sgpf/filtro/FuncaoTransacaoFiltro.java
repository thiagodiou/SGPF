package br.com.sgpf.filtro;

import java.util.List;

import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.FuncaoTransacaoProjeto;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;

public class FuncaoTransacaoFiltro {
	
	public FuncaoTransacaoFiltro(){
		funcaoTransacao = new FuncaoTransacaoProjeto();
	}
	
	private List<Empresa> empresas;
	
	private FuncaoTransacaoProjeto funcaoTransacao;
	
	private List<Projeto> projetos;
		
	private Empresa empresaSelecionada;
	
	private Projeto projetoSelecionado;
	
	private Sistema sistema;

	private String nomeFuncaoTransacao;
	
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	
	public FuncaoTransacaoProjeto getFuncaoTransacao() {
		return funcaoTransacao;
	}

	public void setFuncaoTransacao(FuncaoTransacaoProjeto funcaoTransacao) {
		this.funcaoTransacao = funcaoTransacao;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public String getNomeFuncaoTransacao() {
		return nomeFuncaoTransacao;
	}

	public void setNomeFuncaoTransacao(String nomeFuncaoTransacao) {
		this.nomeFuncaoTransacao = nomeFuncaoTransacao;
	}

	
	
	
}
