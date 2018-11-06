package br.com.sgpf.core.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.FuncaoDadosDAO;
import br.com.sgpf.dao.FuncaoTransacaoDAO;
import br.com.sgpf.dao.ProjetoDAO;
import br.com.sgpf.dao.SistemaDAO;
import br.com.sgpf.enumerator.ElementoContagemEnum;
import br.com.sgpf.enumerator.SimNaoEnum;
import br.com.sgpf.filtro.FuncaoDadosFiltro;
import br.com.sgpf.filtro.FuncaoTransacaoFiltro;
import br.com.sgpf.model.Dado;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.FuncaoTransacaoProjeto;
import br.com.sgpf.model.Projeto;
import br.com.sgpf.model.Sistema;

@Named
@ViewScoped
public class FuncaoTransacaoBean extends AbstractWebBean {
	
	private static final long serialVersionUID = 2020740834590067911L;

	protected static Logger logger = Logger.getLogger(FuncaoTransacaoBean.class);
		
	private List<FuncaoTransacaoProjeto> funcoesTransacao;	
		
	private FuncaoTransacaoProjeto funcaoTransacaoForm;
	
	private FuncaoTransacaoFiltro funcaoTransacaoFiltro;
	
	private EmpresaDAO empresaDAO = new EmpresaDAO();
	
	private FuncaoTransacaoDAO funcaoTransacaoDAO = new FuncaoTransacaoDAO();
	
	private SistemaDAO sistemaDAO = new SistemaDAO();
	
	private ProjetoDAO projetoDAO = new ProjetoDAO();
	
	private FuncaoDadosDAO funcaoDadosDAO = new FuncaoDadosDAO();
	
	private List<Sistema> sistemas = sistemaDAO.listarSistemas();
	
	private List<Projeto> projetos = projetoDAO.listarProjetos();
	
	private List<FuncaoDadosProjeto> funcoesDadosProjetos;
	
	private Set<FuncaoDadosProjeto> funcoesDadosProjetosSelecionadas;
	
	private List<FuncaoTransacaoProjeto> funcoesTransacaoBaseline;
	
	private List<Dado> dadosAdicionadosManualmente;
		
	private Dado dadoForm;
	
	private FuncaoDadosProjeto funcaoDadosProjetoSelecionado;
	
	
	public void preRenderView() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			funcaoTransacaoFiltro = new FuncaoTransacaoFiltro();
			funcaoTransacaoFiltro.getFuncaoTransacao().setProjeto(new Projeto());
			funcaoTransacaoFiltro.setEmpresas(empresaDAO.listarEmpresas());
			funcaoTransacaoFiltro.setProjetos(projetoDAO.listarProjetos());			
			
			setOperacao(OperacaoEnum.FILTRAR);									
			this.funcoesTransacao =  funcaoTransacaoDAO.recuperarFuncaoDadosPorParametro(funcaoTransacaoFiltro);
			dadoForm = new Dado();
		}
	}
	
	public String filtrar() {
		setOperacao(OperacaoEnum.FILTRAR);	
		this.funcoesTransacao =  funcaoTransacaoDAO.recuperarFuncaoDadosPorParametro(funcaoTransacaoFiltro);
		
		if(funcoesTransacao.isEmpty())
			adicionarMensagemAviso("Nehum registro encontrado para os parâmetros informados");
		return null;
	}
	
	public String voltar(){
		setOperacao(OperacaoEnum.FILTRAR);
		return null;
	}
	
	public String limpar() {
		funcaoTransacaoFiltro.setFuncaoTransacao(new FuncaoTransacaoProjeto());	
		funcaoTransacaoFiltro.getFuncaoTransacao().setProjeto(new Projeto());
		return null;
	}
	
	public List<ElementoContagemEnum> getElementosContagem(){		
		return Arrays.asList(ElementoContagemEnum.values()).stream().filter(s -> s.ordinal() > 1).collect(Collectors.toList());		
	}
	
	public String adicionarFuncaoDados(){
		if(funcoesDadosProjetosSelecionadas == null)
			funcoesDadosProjetosSelecionadas = new HashSet<>();
		
		boolean funcaoJaAdicionada = funcoesDadosProjetosSelecionadas.stream().mapToLong(f -> f.getId()).boxed().collect(Collectors.toList()).contains(funcaoDadosProjetoSelecionado.getId());
		if(funcaoJaAdicionada){
			adicionarMensagemErro("ALI já adicionado");
			return null;
		}
			
		funcoesDadosProjetosSelecionadas.add(funcaoDadosProjetoSelecionado);
		return null;
	}
	
	public String buscarFuncaoDados(){
		FuncaoDadosFiltro filtro = new FuncaoDadosFiltro();		 
		filtro.setSistema(funcaoTransacaoForm.getProjeto().getSistema());
		funcoesDadosProjetos = funcaoDadosDAO.recuperarFuncaoDadosPorParametro(filtro);
		return null;
	}
	
	public String buscarDadosBaseline(){
		funcoesTransacaoBaseline = funcaoTransacaoDAO.buscarBaseLinePorSistema(funcaoTransacaoForm.getProjeto().getSistema().getId());		
		return null;
	}
	
	public String criarFuncaoTransacaoApartirDaBaseline(FuncaoTransacaoProjeto funcaoTransacaoBaseline){
		funcaoTransacaoForm = new FuncaoTransacaoProjeto(funcaoTransacaoBaseline);
		
		List<Long> idsDadosBaseLine = funcaoTransacaoBaseline.getTiposDeDados().stream().mapToLong(t->  t.getId()).boxed().collect(Collectors.toList());
		
		funcoesDadosProjetosSelecionadas = funcaoTransacaoBaseline.getArquivosReferenciados();
		
		funcoesDadosProjetosSelecionadas.forEach(f -> {
			f.getTiposDeRegistros().forEach(t -> {
				t.getDados().forEach(d -> d.setSelecionado(idsDadosBaseLine.contains(d.getId())));
			});
		});
	
		return null;
	}
	
	public String excluirALI(FuncaoDadosProjeto fdp){
		Iterator<FuncaoDadosProjeto> iterator = this.funcoesDadosProjetosSelecionadas.iterator();
		while(iterator.hasNext()){
			FuncaoDadosProjeto f = iterator.next();
			if(f.getId().equals(fdp.getId())){
				iterator.remove();
			}
				
		}
		
		return null;
	}
	
	public String adicionarDadoManual(){
		if(dadoForm.getNome() == null || dadoForm.getNome().isEmpty()){
			adicionarMensagemErro("Nome do Dado é obrigatório");
			return null;
		}
		boolean registroDuplicado = this.funcaoTransacaoForm.getTiposDeDados().stream().filter(t-> t.equals(dadoForm)).count() > 0;
		if(registroDuplicado){
			adicionarMensagemErro("Dado já cadastrado na Função de Transação.");
			return null;
		}
		
		dadoForm.setAdicionadoManualmente(true);
		this.funcaoTransacaoForm.getTiposDeDados().add(dadoForm);
		dadoForm = new Dado();
		return null;
	}
	
	public String salvar(){
		try{							
			
			List<Dado> dadosManual = getDadosAdicionadosManualmente();
			
			funcaoTransacaoForm.setTiposDeDados(new ArrayList<>());
			funcoesDadosProjetosSelecionadas.forEach(f -> {
				f.getTiposDeRegistros().forEach(t -> {
					funcaoTransacaoForm.adicionarTipoDeDados(t.getDados().stream().filter(d -> d.isSelecionado() || d.isAdicionadoManualmente()).collect(Collectors.toList()));
				});
			});
			
			if(dadosManual != null)
				funcaoTransacaoForm.adicionarTipoDeDados(dadosManual);
			validar();
			funcaoTransacaoForm.setArquivosReferenciados(funcoesDadosProjetosSelecionadas);
			funcaoTransacaoDAO.salvar(funcaoTransacaoForm);
			
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
	
	public String excluirDado(Dado dado) {
		Iterator<Dado> iteratorDado = funcaoTransacaoForm.getTiposDeDados().iterator();
		while(iteratorDado.hasNext()){
			Dado d = iteratorDado.next();			
			if(d.isAdicionadoManualmente() && d.equals(dado))
				iteratorDado.remove();
		}
		
		return null;
	}
	
	private void validar() throws Exception{		 
		boolean funcaoTransacaoJaCadastradaParaProjeto = false;
		boolean nenhumTipoDadoSelecionado = false;
		boolean ehConsultaExterna = false;
		boolean projetoConcluido = false;
		
		
		
		List<FuncaoTransacaoProjeto> ftpComMesmoNome = funcaoTransacaoDAO.buscarPorNomeExatoEProjeto(funcaoTransacaoForm.getProjeto().getId(), funcaoTransacaoForm.getNome());
		funcaoTransacaoJaCadastradaParaProjeto = ftpComMesmoNome.stream().filter(f -> f.getId() != funcaoTransacaoForm.getId()).count() > 0;
		if(funcaoTransacaoJaCadastradaParaProjeto)
			throw new RuntimeException("Função de Transação já existe para o projeto");
		
		
		nenhumTipoDadoSelecionado = funcaoTransacaoForm.getTiposDeDados().isEmpty();
		if(nenhumTipoDadoSelecionado)
			throw new RuntimeException("Função de Transação sem nenhum tipo de dado");
		
		projetoConcluido = funcaoTransacaoForm.getProjeto().isConcluido();
		if(projetoConcluido)
			throw new RuntimeException("Não se pode alterar um projeto já concluído");
		
		ehConsultaExterna = funcaoTransacaoForm.getElementoContagem().equals(ElementoContagemEnum.CE);
		boolean nenhumTipoDeDado = funcaoTransacaoForm.getTiposDeDados() == null ||funcaoTransacaoForm.getTiposDeDados().stream().filter(s-> !s.isAdicionadoManualmente()).count() == 0;
		if(ehConsultaExterna && nenhumTipoDeDado)
			throw new RuntimeException("Consultar externa sem arquivo referenciado cadastrado.");			
			
		if(ehConsultaExterna && funcaoTransacaoForm.getTiposDeDados().stream().filter(s-> s.isDadoDerivado()).count() >0){
			throw new RuntimeException("Consultar externa não permite dado derivado");
		}
	}
	
	public String excluir(FuncaoTransacaoProjeto ftp){
		
		if(ftp.getProjeto().isConcluido()){
			adicionarMensagemErro("Função de Transação pertence a um projeto finalizado.");
			return null;
		}
			
		if(ftp.getPerteceBaseline() != null && ftp.getPerteceBaseline().equals(SimNaoEnum.SIM)){
			adicionarMensagemErro("Operação não permitida, pois Função de Transação pertence a baseline");
			return null;
		}
		
		try{
			funcaoTransacaoDAO.excluir(ftp);
		}catch(Exception e){
			e.printStackTrace();
			adicionarMensagemErro("Erro ao efetuar operação");
		}
		return filtrar();
	}
	
	
	public String incluir() {
		setOperacao(OperacaoEnum.INCLUIR);
		funcaoTransacaoForm = new FuncaoTransacaoProjeto();		
		funcoesDadosProjetos = new ArrayList<>();
		dadoForm = new Dado();
		funcoesDadosProjetosSelecionadas = new HashSet<>();
		
		List<Dado> dadosPadrao = new ArrayList<>();
		Dado comando = new Dado();
		comando.setAdicionadoManualmente(true);
		comando.setNome("Comando");
		
		Dado mensagem = new Dado();
		mensagem.setAdicionadoManualmente(true);
		mensagem.setNome("Mensagem");
		
		dadosPadrao.add(comando);
		dadosPadrao.add(mensagem);
		
		
		funcaoTransacaoForm.setTiposDeDados(dadosPadrao);
		return null;
	}
	
	public String alterar(FuncaoTransacaoProjeto funcaoTransacao) {
		setOperacao(OperacaoEnum.ALTERAR);
		funcaoTransacaoForm = funcaoTransacao;
		dadoForm = new Dado();
		buscarFuncaoDados();
		List<Long> idsDadosDaFuncaoTransacao = funcaoTransacaoForm.getTiposDeDados().stream().mapToLong(s -> s.getId()).boxed().collect(Collectors.toList());
		
		funcoesDadosProjetosSelecionadas = new HashSet<>();
		funcoesDadosProjetosSelecionadas.addAll(funcaoTransacao.getArquivosReferenciados());
		
		funcoesDadosProjetosSelecionadas.forEach(s -> {
			s.setSelecionado(idsDadosDaFuncaoTransacao.contains(s.getId()));		
		});			
		
		funcoesDadosProjetosSelecionadas.forEach(f -> {
			f.getTiposDeRegistros().forEach(t -> {
				t.getDados().forEach(d -> d.setSelecionado(idsDadosDaFuncaoTransacao.contains(d.getId())));
			});
		});
		
		
		
		return null;
	}	
	
	public List<FuncaoTransacaoProjeto> getFuncoesTransacao() {
		return funcoesTransacao;
	}

	public void setFuncoesTransacao(List<FuncaoTransacaoProjeto> funcoesTransacao) {
		this.funcoesTransacao = funcoesTransacao;
	}

	public FuncaoTransacaoFiltro getFuncaoTransacaoFiltro() {
		return funcaoTransacaoFiltro;
	}

	public void setFuncaoTransacaoFiltro(FuncaoTransacaoFiltro funcaoTransacaoFiltro) {
		this.funcaoTransacaoFiltro = funcaoTransacaoFiltro;
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

	public FuncaoTransacaoProjeto getFuncaoTransacaoForm() {
		return funcaoTransacaoForm;
	}

	public void setFuncaoTransacaoForm(FuncaoTransacaoProjeto funcaoTransacaoForm) {
		this.funcaoTransacaoForm = funcaoTransacaoForm;
	}

	public List<FuncaoDadosProjeto> getFuncoesDadosProjetos() {
		return funcoesDadosProjetos;
	}

	public void setFuncoesDadosProjetos(List<FuncaoDadosProjeto> funcoesDadosProjetos) {
		this.funcoesDadosProjetos = funcoesDadosProjetos;
	}

	public List<FuncaoTransacaoProjeto> getFuncoesTransacaoBaseline() {
		return funcoesTransacaoBaseline;
	}

	public void setFuncoesTransacaoBaseline(List<FuncaoTransacaoProjeto> funcoesTransacaoBaseline) {
		this.funcoesTransacaoBaseline = funcoesTransacaoBaseline;
	}

	public Dado getDadoForm() {
		return dadoForm;
	}

	public void setDadoForm(Dado dadoForm) {
		this.dadoForm = dadoForm;
	}

	public List<Dado> getDadosAdicionadosManualmente() {
		if(funcaoTransacaoForm.getTiposDeDados() != null)
			dadosAdicionadosManualmente = funcaoTransacaoForm.getTiposDeDados().stream().filter(t-> t.isAdicionadoManualmente()).collect(Collectors.toList());
		else
			dadosAdicionadosManualmente  = new ArrayList<>();
		return dadosAdicionadosManualmente;
	}

	public void setDadosAdicionadosManualmente(List<Dado> dadosAdicionadosManualmente) {
		this.dadosAdicionadosManualmente = dadosAdicionadosManualmente;
	}

	public FuncaoDadosProjeto getFuncaoDadosProjetoSelecionado() {
		return funcaoDadosProjetoSelecionado;
	}

	public void setFuncaoDadosProjetoSelecionado(FuncaoDadosProjeto funcaoDadosProjetoSelecionado) {
		this.funcaoDadosProjetoSelecionado = funcaoDadosProjetoSelecionado;
	}

	public Set<FuncaoDadosProjeto> getFuncoesDadosProjetosSelecionadas() {
		return funcoesDadosProjetosSelecionadas;
	}

	public void setFuncoesDadosProjetosSelecionadas(Set<FuncaoDadosProjeto> funcoesDadosProjetosSelecionadas) {
		this.funcoesDadosProjetosSelecionadas = funcoesDadosProjetosSelecionadas;
	}

	
	
	

}
