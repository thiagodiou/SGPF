package br.com.sgpf.core.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.FuncaoDadosDAO;
//import br.com.sgpf.dao.AnalistaDAO;
//import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.ProjetoDAO;
import br.com.sgpf.dao.SistemaDAO;
import br.com.sgpf.enumerator.ElementoContagemEnum;
import br.com.sgpf.filtro.FuncaoDadosFiltro;
import br.com.sgpf.model.Dado;
//import br.com.sgpf.model.Analista;
//import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;
import br.com.sgpf.model.TipoRegistro;

@Named
@ViewScoped
public class FuncaoDadosBean extends AbstractWebBean {
	
	private static final long serialVersionUID = 2020740834590067911L;

	protected static Logger logger = Logger.getLogger(FuncaoDadosBean.class);
		
	private List<FuncaoDadosProjeto> funcoesDados;	
		
	private FuncaoDadosProjeto funcaoDadosForm;
	
	//private FuncaoDadosProjeto funcaoDadosFiltro;
	
	private FuncaoDadosFiltro funcaoDadosFiltro;
	
	private TipoRegistro tipoRegistroForm;
	
	private Dado dadoForm;
	
	private TipoRegistro tipoRegistroSelecionado;
	
	private EmpresaDAO empresaDAO = new EmpresaDAO();
	
	private FuncaoDadosDAO funcaoDadosDAO = new FuncaoDadosDAO();
	
	private SistemaDAO sistemaDAO = new SistemaDAO();
	
	private ProjetoDAO projetoDAO = new ProjetoDAO();
	
	private List<Sistema> sistemas = sistemaDAO.listarSistemas();
	
	private List<Projeto> projetos = projetoDAO.listarProjetos();
	
	private List<FuncaoDadosProjeto> funcoesDadosBaseline;
	
	public void preRenderView() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			funcaoDadosFiltro = new FuncaoDadosFiltro();
			funcaoDadosFiltro.getFuncaoDados().setProjeto(new Projeto());
			funcaoDadosFiltro.setEmpresas(empresaDAO.listarEmpresas());
			funcaoDadosFiltro.setProjetos(projetoDAO.listarProjetos());			
			
			setOperacao(OperacaoEnum.FILTRAR);			
			this.funcoesDados =  funcaoDadosDAO.listarFuncaoDados();
			tipoRegistroForm = new TipoRegistro();
			dadoForm = new Dado();
		}
	}
	
	public String voltar(){
		setOperacao(OperacaoEnum.FILTRAR);
		return null;
	}
	
	public String filtrar() {
		setOperacao(OperacaoEnum.FILTRAR);	
		this.funcoesDados =  funcaoDadosDAO.recuperarFuncaoDadosPorParametro(funcaoDadosFiltro);
		
		if(funcoesDados.isEmpty())
			adicionarMensagemAviso("Nehum registro encontrado para os parâmetros informados");
		return null;
	}
	
	public String limpar() {
		funcaoDadosFiltro.setFuncaoDados(new FuncaoDadosProjeto());	
		funcaoDadosFiltro.getFuncaoDados().setProjeto(new Projeto());
		return null;
	}
	
	
	public String incluir() {
		setOperacao(OperacaoEnum.INCLUIR);
		tipoRegistroForm = new TipoRegistro();
		funcaoDadosForm = new FuncaoDadosProjeto();		
		return null;
	}
	
	public String alterar(FuncaoDadosProjeto funcaoDadosProjeto) {
		setOperacao(OperacaoEnum.ALTERAR);
		funcaoDadosForm = funcaoDadosProjeto;		
		return null;
	}	

	
	public String visualizar(FuncaoDadosProjeto fdp){ 
		return null;
	}

	
	/* Manipulando tipo de registro*/
	public String inserirTipoRegistro(){
		if(tipoRegistroForm.getNome() == null || tipoRegistroForm.getNome().isEmpty()){
			adicionarMensagemAviso("Favor informar o nome do tipo de registro");	
			return null;
		}
		
		if(funcaoDadosForm.getTiposDeRegistros() == null)
			funcaoDadosForm.setTiposDeRegistros(new ArrayList<>());
		
		funcaoDadosForm.getTiposDeRegistros().add(tipoRegistroForm);
		tipoRegistroForm = new TipoRegistro();
		
		return null;
	}
	
	public String excluirTipoRegistro(TipoRegistro tr) {
		Iterator<TipoRegistro> iteratorDado = funcaoDadosForm.getTiposDeRegistros().iterator();
		while(iteratorDado.hasNext()){
			TipoRegistro tipo = iteratorDado.next();
			if(tipo.equals(tr))
				iteratorDado.remove();
		}
		
		return null;
	}
	
	
	/* Manipulando Dado */
	public String inserirDado(){		
		//TODO: validacoes
		
		if(tipoRegistroSelecionado.getDados() == null)
			tipoRegistroSelecionado.setDados(new ArrayList<>());
		
		tipoRegistroSelecionado.getDados().add(dadoForm);
		
		dadoForm = new Dado();
		return null;
	}
	
	public String excluirDado(Dado dado){
		Iterator<Dado> iteratorDado = tipoRegistroSelecionado.getDados().iterator();
		while(iteratorDado.hasNext()){
			Dado d = iteratorDado.next();
			if(d.equals(dado))
				iteratorDado.remove();
		}
		return null;
	}
	
	
	/* Manipulando Função de dados */
	public String salvar(){
		try{		
			validar();
			funcaoDadosDAO.salvarFuncaoDados(this.funcaoDadosForm);
			setOperacao(OperacaoEnum.FILTRAR);					
		}catch(RuntimeException e){
			adicionarMensagemErro(e.getMessage());
			return null;
		}
		catch(Exception e){
			adicionarMensagemAviso("Erro ao realizar operação.");
			return null;			
		}
		
		adicionarMensagemInfo("Operação realizada com sucesso");
				
		return filtrar();
	}	
		
	public String criarFuncaoDadosApartirDaBaseline(FuncaoDadosProjeto funcaoDadoBaseline){
		funcaoDadosForm = new FuncaoDadosProjeto(funcaoDadoBaseline);		
		return null;
	}
	
	
	private void validar() throws Exception{		  
		boolean projetoComMesmoNome = false;
		boolean funcaoDadosSemTipoDeRegistro = false;
		boolean tipoRegistroSemAtributo = false;
				
		if(funcaoDadosForm.getProjeto().isConcluido())
			throw new RuntimeException("Não se pode incluir/alterar um projeto já concluído");
			
		funcaoDadosSemTipoDeRegistro = funcaoDadosForm.getTiposDeRegistros() == null || funcaoDadosForm.getTiposDeRegistros().isEmpty(); 		
		if(funcaoDadosSemTipoDeRegistro)
			throw new RuntimeException("Função de dados sem Tipo de Registro");			
		
				
		tipoRegistroSemAtributo = funcaoDadosForm.getTiposDeRegistros().stream().filter(f -> f.getDados() == null || f.getDados().isEmpty())
				.count() > 0;			
		if(tipoRegistroSemAtributo)
			throw new RuntimeException("Tipo de registro sem atributo");
		
			
		List<FuncaoDadosProjeto> fdpComMesmoNome = funcaoDadosDAO.buscarPorNomeExatoEProjeto(funcaoDadosForm.getProjeto().getId(), funcaoDadosForm.getNome());
		projetoComMesmoNome = fdpComMesmoNome.stream().filter(f -> f.getId() != funcaoDadosForm.getId()).count() > 0;
		if(projetoComMesmoNome)
			throw new RuntimeException("Função de dados já existe para o projeto.");			
		
	}
	
	public String excluir(FuncaoDadosProjeto fdp){
		
		if(fdp.getProjeto().isConcluido()){
			adicionarMensagemErro("Função de dados pertence a um projeto finalizado.");
			return null;
		}
			
		
		try{
			funcaoDadosDAO.excluirFuncaoDados(fdp);
		}catch(Exception e){
			e.printStackTrace();
			adicionarMensagemErro("Erro ao efetuar operação");
		}
		return filtrar();
	}
	
	public String buscarDadosBaseline(){
		funcoesDadosBaseline = funcaoDadosDAO.buscarBaseLinePorSistema(funcaoDadosForm.getProjeto().getSistema().getId());		
		return null;
	}
	
	public List<ElementoContagemEnum> getElementosContagem(){		
		return Arrays.asList(ElementoContagemEnum.values()).stream().filter(s -> s.ordinal() <2).collect(Collectors.toList());			
	}
	
	public FuncaoDadosProjeto getFuncaoDadosForm() {
		return funcaoDadosForm;
	}


	public void setFuncaoDadosForm(FuncaoDadosProjeto funcaoDadosForm) {
		this.funcaoDadosForm = funcaoDadosForm;
	}
	
	public FuncaoDadosFiltro getFuncaoDadosFiltro() {
		return funcaoDadosFiltro;
	}

	public void setFuncaoDadosFiltro(FuncaoDadosFiltro funcaoDadosFiltro) {
		this.funcaoDadosFiltro = funcaoDadosFiltro;
	}

	public List<Sistema> getSistemas() {
		return sistemas;
	}


	public void setSistemas(List<Sistema> sistemas) {
		this.sistemas = sistemas;
	}


	public List<Projeto> getProjetos() {
		return projetos;
	}


	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
	
	public List<FuncaoDadosProjeto> getFuncoesDados() {
		return funcoesDados;
	}


	public void setFuncoesDados(List<FuncaoDadosProjeto> funcoesDados) {
		this.funcoesDados = funcoesDados;
	}


	public TipoRegistro getTipoRegistroForm() {
		return tipoRegistroForm;
	}


	public void setTipoRegistroForm(TipoRegistro tipoRegistroForm) {
		this.tipoRegistroForm = tipoRegistroForm;
	}

	public TipoRegistro getTipoRegistroSelecionado() {
		return tipoRegistroSelecionado;
	}

	public void setTipoRegistroSelecionado(TipoRegistro tipoRegistroSelecionado) {
		this.tipoRegistroSelecionado = tipoRegistroSelecionado;
	}
	public void selecionarTipoRegistro(TipoRegistro tr){
		dadoForm = new Dado();
		this.tipoRegistroSelecionado = tr;
	}

	public Dado getDadoForm() {
		return dadoForm;
	}

	public void setDadoForm(Dado dadoForm) {
		this.dadoForm = dadoForm;
	}

	public List<FuncaoDadosProjeto> getFuncoesDadosBaseline() {
		return funcoesDadosBaseline;
	}

	public void setFuncoesDadosBaseline(List<FuncaoDadosProjeto> funcoesDadosBaseline) {
		this.funcoesDadosBaseline = funcoesDadosBaseline;
	}
	
}
