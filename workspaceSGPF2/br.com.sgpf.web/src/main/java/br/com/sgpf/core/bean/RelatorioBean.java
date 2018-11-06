package br.com.sgpf.core.bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.FuncaoDadosDAO;
import br.com.sgpf.dao.FuncaoTransacaoDAO;
import br.com.sgpf.dao.ProjetoDAO;
import br.com.sgpf.dao.SistemaDAO;
import br.com.sgpf.enumerator.TipoRelatorioEnum;
import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.FuncaoTransacaoProjeto;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;
import br.com.sgpf.vo.RelatorioVO;

@Named
@ViewScoped
public class RelatorioBean extends AbstractWebBean {


	private static final long serialVersionUID = 8317616840366558690L;

	private TipoRelatorioEnum tipoRelatorio;
	
	private RelatorioVO relatorioVO;
	
	private Projeto projetoSelecionado;
	
	private List<Projeto> projetos;
	
	private ProjetoDAO projetoDAO = new ProjetoDAO();
	
	private EmpresaDAO empresaDAO = new EmpresaDAO();
	
	private FuncaoDadosDAO funcaoDadosDAO = new FuncaoDadosDAO();
	
	private FuncaoTransacaoDAO funcaoTransacaoDAO = new FuncaoTransacaoDAO();
	
	private List<Empresa> empresas = empresaDAO.listarEmpresas();	
	
	private Empresa empresaSelecionada;
	
	private Sistema sistemaSelecionado;
	
	
	public void preRenderView() {		
		if (!FacesContext.getCurrentInstance().isPostback()) {		
			projetos = projetoDAO.listarProjetos();
			setOperacao(OperacaoEnum.FILTRAR);
		}
	}
	
	public String gerarRelatorio(){
		
		if(tipoRelatorio == null){
			adicionarMensagemErro("Favor informar um tipo de relatório");
			return null;
		}
			
		relatorioVO = new RelatorioVO();
		
		if(tipoRelatorio.equals(TipoRelatorioEnum.POR_PROJETO)){
			
			if(projetoSelecionado == null){
				adicionarMensagemErro("Favor informar um Projeto");
				return null;
			}
				
			if (projetoSelecionado.getFuncoesDados() == null || projetoSelecionado.getFuncoesDados().isEmpty()
					|| projetoSelecionado.getFuncoesTransacao() == null
					|| projetoSelecionado.getFuncoesTransacao().isEmpty()){
				adicionarMensagemErro("Projeto sem função de dados ou transação");
				return null;
			}
			
			relatorioVO.setAnalista(projetoSelecionado.getAnalista().getNome());
			relatorioVO.setData(new Date());
			relatorioVO.setFuncoesDados(projetoSelecionado.getFuncoesDados());
			relatorioVO.setFuncoesTransacao(projetoSelecionado.getFuncoesTransacao());
			relatorioVO.setIdentificadoProjeto(projetoSelecionado.getIdentificador());
			relatorioVO.setNomeEmpresa(projetoSelecionado.getSistema().getEmpresa().getNomeFantasia());
			relatorioVO.setSiglaSistema(projetoSelecionado.getSistema().getSigla());
			relatorioVO.setSistema(projetoSelecionado.getSistema().getNome());
			relatorioVO.setTituloProjeto(projetoSelecionado.getTitulo());			
		}else{
			if(empresaSelecionada == null || sistemaSelecionado == null){
				adicionarMensagemErro("Favor informar uma Empresa e um Sistema");
				return null;
			}
			
			
			List<FuncaoDadosProjeto> funcoesDadosBaseline = funcaoDadosDAO.buscarBaseLinePorSistema(sistemaSelecionado.getId());
			List<FuncaoTransacaoProjeto> funcaoTransacaoBaseline = funcaoTransacaoDAO.buscarBaseLinePorSistema(sistemaSelecionado.getId());
			
			if (funcoesDadosBaseline == null || funcoesDadosBaseline.isEmpty()
					|| funcaoTransacaoBaseline == null
					|| funcaoTransacaoBaseline.isEmpty()){
				adicionarMensagemErro("Sistema sem função de dados ou transação");
				return null;
			}
			
			relatorioVO.setNomeEmpresa(empresaSelecionada.getNomeFantasia());
			relatorioVO.setSiglaSistema(sistemaSelecionado.getSigla());
			relatorioVO.setSistema(sistemaSelecionado.getNome());
			relatorioVO.setFuncoesDados(funcoesDadosBaseline);
			relatorioVO.setData(new Date());
			if(funcaoTransacaoBaseline != null)
				relatorioVO.setFuncoesTransacao(funcaoTransacaoBaseline.stream().filter(f -> !f.isConversaoDados()).collect(Collectors.toList()));
		}
		
		return null;
	}
	
	public List<TipoRelatorioEnum> getTipoRelorios(){		
		return Arrays.asList(TipoRelatorioEnum.values());		
	}

	public TipoRelatorioEnum getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorioEnum tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public RelatorioVO getRelatorioVO() {
		return relatorioVO;
	}

	public void setRelatorioVO(RelatorioVO relatorioVO) {
		this.relatorioVO = relatorioVO;
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

	public void setEmpresaSelecionada(Empresa empresaSelecioanada) {
		this.empresaSelecionada = empresaSelecioanada;
	}

	public Sistema getSistemaSelecionado() {
		return sistemaSelecionado;
	}

	public void setSistemaSelecionado(Sistema sistemaSelecionado) {
		this.sistemaSelecionado = sistemaSelecionado;
	}

	
}
