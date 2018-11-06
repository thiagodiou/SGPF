package br.com.sgpf.filtro;

import java.util.List;

import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;

public class FuncaoDadosFiltro {
	
	public FuncaoDadosFiltro(){
		funcaoDados = new FuncaoDadosProjeto();
	}
	
	private List<Empresa> empresas;
	
	private FuncaoDadosProjeto funcaoDados;
	
	private List<Projeto> projetos;
		
	private Empresa empresaSelecionada;
	
	private Projeto projetoSelecionado;
	
	private Sistema sistema;

	private String nomeFuncaoDados;
	
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public FuncaoDadosProjeto getFuncaoDados() {
		return funcaoDados;
	}

	public void setFuncaoDados(FuncaoDadosProjeto funcaoDados) {
		this.funcaoDados = funcaoDados;
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

	public String getNomeFuncaoDados() {
		return nomeFuncaoDados;
	}

	public void setNomeFuncaoDados(String nomeFuncaoDados) {
		this.nomeFuncaoDados = nomeFuncaoDados;
	}
	
	
}
