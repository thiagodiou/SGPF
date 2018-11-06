package br.com.sgpf.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sgpf.model.Dado;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.FuncaoTransacaoProjeto;
import br.com.sgpf.model.TipoRegistro;

public class RelatorioVO {
	
	
	private Date data;
	private String nomeEmpresa;
	private String siglaSistema;
	private String sistema;
	private String identificadoProjeto;
	private String tituloProjeto;
	private String analista;
	private List<FuncaoDadosProjeto> funcoesDados;
	private List<FuncaoTransacaoProjeto> funcoesTransacao;
	private List<FuncaoEDadoVO> linhas = new ArrayList<>();
	private long valorPF;
	
	public void setFuncoesDados(List<FuncaoDadosProjeto> f){
		this.funcoesDados = f;
		for(FuncaoDadosProjeto fd: funcoesDados){
			valorPF += fd.getTamanhoPF();
			FuncaoEDadoVO linha = new FuncaoEDadoVO();
			linha.setComplexidade(fd.recuperarComplexidade().getDescricao());
			linha.setElementoContagem(fd.getElementoContagem().getDescricao());
			linha.setNome(fd.getNome());
			long quantidadeTD = 0l;
			if(fd.getTiposDeRegistros() != null){
				for(TipoRegistro tr:fd.getTiposDeRegistros())
					quantidadeTD += tr.getDados().size();
				
				linha.setQuantidadeTR(fd.getTiposDeRegistros() == null ? 0l : new Long(fd.getTiposDeRegistros().size()));
			}else
				linha.setQuantidadeTR(0l);
			linha.setQuantidadeTD(quantidadeTD);
			linha.setValorPF(new Long(fd.getTamanhoPF()));
			linhas.add(linha);
		}
		
		
	}
	
	public void setFuncoesTransacao(List<FuncaoTransacaoProjeto> funcoesTransacao){
		this.funcoesTransacao = funcoesTransacao;
		for(FuncaoTransacaoProjeto fd: this.funcoesTransacao){
			valorPF += fd.getTamanhoPF();
			
			FuncaoEDadoVO linha = new FuncaoEDadoVO();
			linha.setComplexidade(fd.recuperarComplexidade().getDescricao());
			linha.setElementoContagem(fd.getElementoContagem().getDescricao());
			linha.setNome(fd.getNome());
			long quantidadeTD = 0l;
			if(fd.getTiposDeDados() != null){
				
				List<Long> idsTrs = new ArrayList<>();
				
				for(Dado d : fd.getTiposDeDados()){
					if(d.getTipoRegistro() == null) continue;
					Long id = d.getTipoRegistro().getFuncaoDadosProjeto().getId();//getId();
					if(!idsTrs.contains(id))
						idsTrs.add(id);
				}
				
				
				quantidadeTD = fd.getTiposDeDados().size();
				
				linha.setQuantidadeTR(new Long(idsTrs.size()));
				linha.setQuantidadeTD(quantidadeTD);
			}else{
				linha.setQuantidadeTR(0l);
				linha.setQuantidadeTD(0l);
			}
				
			
			linha.setValorPF(new Long(fd.getTamanhoPF()));
			linhas.add(linha);
		}
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public String getSiglaSistema() {
		return siglaSistema;
	}

	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getIdentificadoProjeto() {
		return identificadoProjeto;
	}

	public void setIdentificadoProjeto(String identificadoProjeto) {
		this.identificadoProjeto = identificadoProjeto;
	}

	public String getTituloProjeto() {
		return tituloProjeto;
	}

	public void setTituloProjeto(String tituloProjeto) {
		this.tituloProjeto = tituloProjeto;
	}

	public String getAnalista() {
		return analista;
	}

	public void setAnalista(String analista) {
		this.analista = analista;
	}

	public long getValorPF() {
		return valorPF;
	}

	public void setValorPF(long valorPF) {
		this.valorPF = valorPF;
	}

	public List<FuncaoDadosProjeto> getFuncoesDados() {
		return funcoesDados;
	}

	public List<FuncaoTransacaoProjeto> getFuncoesTransacao() {
		return funcoesTransacao;
	}

	public List<FuncaoEDadoVO> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<FuncaoEDadoVO> linhas) {
		this.linhas = linhas;
	}
	
	
	
}
