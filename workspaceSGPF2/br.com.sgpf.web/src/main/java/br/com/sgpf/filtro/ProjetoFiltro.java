package br.com.sgpf.filtro;

import java.util.ArrayList;
import java.util.List;

import br.com.sgpf.model.Analista;
import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;

public class ProjetoFiltro {
	
	public ProjetoFiltro(){
		projeto = new Projeto();
		analistas = new ArrayList<>();	
		empresas = new ArrayList<>();
		empresaSelecionada = new Empresa();
	}
	
	private Projeto projeto;
	
	private List<Analista> analistas;
		
	private List<Empresa> empresas;
	
	private Empresa empresaSelecionada;

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<Analista> getAnalistas() {
		return analistas;
	}

	public void setAnalistas(List<Analista> analistas) {
		this.analistas = analistas;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Empresa getEmpresaSelecionada() {		
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {	
		this.empresaSelecionada = empresaSelecionada;
	}
	
	//projeto.sistema.empresa = empresaSelecioanda
	
	
}
