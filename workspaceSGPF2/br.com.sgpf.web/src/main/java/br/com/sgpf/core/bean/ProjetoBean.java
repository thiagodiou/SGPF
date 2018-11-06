package br.com.sgpf.core.bean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.AnalistaDAO;
import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.FuncaoDadosDAO;
import br.com.sgpf.dao.FuncaoTransacaoDAO;
import br.com.sgpf.dao.ProjetoDAO;
import br.com.sgpf.dao.SistemaDAO;
import br.com.sgpf.enumerator.SimNaoEnum;
import br.com.sgpf.filtro.ProjetoFiltro;
import br.com.sgpf.model.Analista;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;

@Named
@ViewScoped
public class ProjetoBean extends AbstractWebBean {

	private static final long serialVersionUID = 2020740844590067911L;

	protected static Logger logger = Logger.getLogger(ProjetoBean.class);

	private List<Projeto> projetos;

	private Projeto projetoForm;

	private ProjetoFiltro projetoFiltro;

	private List<Analista> analistas;;

	private SistemaDAO sistemaDAO = new SistemaDAO();

	private ProjetoDAO projetoDAO = new ProjetoDAO();

	private EmpresaDAO empresaDAO = new EmpresaDAO();

	private AnalistaDAO analistaDAO = new AnalistaDAO();
	
	private FuncaoDadosDAO funcaoDadosDAO = new FuncaoDadosDAO();
	
	private FuncaoTransacaoDAO funcaoTransacaoDAO = new FuncaoTransacaoDAO();

	public void preRenderView() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			projetoFiltro = new ProjetoFiltro();
			atualizarCombos();
			setOperacao(OperacaoEnum.FILTRAR);
			this.projetos = projetoDAO.listarProjetos();

		}
	}

	private void atualizarCombos() {
		projetoFiltro.setAnalistas(analistaDAO.listarAnalistas());
		projetoFiltro.setEmpresas(empresaDAO.listarEmpresas());
	}

	public String concluirProjeto(Projeto projeto){
		if(projeto.isConcluido()){
			adicionarMensagemErro("Projeto já concluído");
			return null;
		}
		try{
			funcaoDadosDAO.atualizarBaselineAnterior(projeto.getSistema().getId());
			funcaoTransacaoDAO.atualizarBaselineAnterior(projeto.getSistema().getId());
			projeto.concluir();
			projetoDAO.salvarProjeto(projeto);
		}catch(RuntimeException e){
			adicionarMensagemErro(e.getMessage());
		}catch(Exception e){
			adicionarMensagemErro("Erro ao realizar operação.");
		}
		
		adicionarMensagemInfo("Operação realizada com sucesso");
		return null;
	}

	public String filtrar() {
		setOperacao(OperacaoEnum.FILTRAR);
		if (projetoFiltro.getProjeto().getSistema() == null)
			projetoFiltro.getProjeto().setSistema(new Sistema());
		projetoFiltro.getProjeto().getSistema().setEmpresa(projetoFiltro.getEmpresaSelecionada());

		this.projetos = projetoDAO.recuperarProjetoPorParametro(projetoFiltro.getProjeto());
		return null;
	}

	public String limpar() {
		projetoFiltro = new ProjetoFiltro();
		atualizarCombos();
		return null;
	}

	public String incluir() {
		setOperacao(OperacaoEnum.INCLUIR);
		projetoForm = new Projeto();
		return null;
	}

	public String salvar() {
		try {

			// validar();
			projetoDAO.salvarProjeto(this.projetoForm);
			setOperacao(OperacaoEnum.FILTRAR);

			// validarAlteracao();
			// TODO: CHAMA DAO
		} catch (Exception e) {
			adicionarMensagemAviso("Projeto já cadastrado!");
			return null;

		}
		adicionarMensagemInfo("Operação realizada com sucesso");
		return filtrar();
	}

	private void validar() throws Exception {

		if (getOperacao() == OperacaoEnum.INCLUIR) {
			List<Projeto> projetos = projetoDAO.recuperarProjetoPorParametro(this.projetoForm);
			if (projetos.size() > 0) {
				throw new Exception("Projeto já cadastrado!");
			}
		}
	}

	public String alterar(Projeto projeto) {
		setOperacao(OperacaoEnum.ALTERAR);
		projetoForm = projeto;
		return null;
	}

	public String excluir(Sistema sistema) {
		return null;
	}

	public List<Projeto> getProjetos() {
		return this.projetos;
	}

	public String visualizarProjeto(Projeto projeto) {
		return null;
	}

	public String alterarProjeto(Projeto projeto) {
		setOperacao(OperacaoEnum.ALTERAR);
		projetoForm = projeto;
		return null;
	}

	public String excluirProjeto(Projeto projeto) {
		try {
			projetoDAO.excluirProjeto(projeto);
		} catch (Exception e) {
			e.printStackTrace();
			adicionarMensagemErro("DEU MERDA");
		}
		return filtrar();
	}

	public ProjetoFiltro getProjetoFiltro() {
		return projetoFiltro;
	}

	public void setProjetoFiltro(ProjetoFiltro projetoFiltro) {
		this.projetoFiltro = projetoFiltro;
	}

	public Projeto getProjetoForm() {
		return this.projetoForm;
	}

	public void setProjetoForm(Projeto projeto) {
		this.projetoForm = projeto;
	}

}
