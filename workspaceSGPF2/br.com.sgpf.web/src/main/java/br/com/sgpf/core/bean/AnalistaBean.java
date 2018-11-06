package br.com.sgpf.core.bean;	

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.AnalistaDAO;
import br.com.sgpf.model.Analista;


@Named
@ViewScoped
public class AnalistaBean extends AbstractWebBean {

	private static final long serialVersionUID = 5653645873997247062L;

	protected static Logger logger = Logger.getLogger(AnalistaBean.class);
		
	private List<Analista> analistas;	
		
	private Analista analistaForm;
	
	private Analista analistaFiltro;
	
	private AnalistaDAO analistaDAO = new AnalistaDAO();
		
	
	public void preRenderView() 
	{		
		if (!FacesContext.getCurrentInstance().isPostback()) 
		{			
			analistaFiltro = new Analista();
			setOperacao(OperacaoEnum.FILTRAR);			
			this.analistas =  analistaDAO.listarAnalistas();
		}
	}
	
	
	public String filtrar() 
	{
		setOperacao(OperacaoEnum.FILTRAR);	
		this.analistas =  analistaDAO.recuperarAnalistaPorParametro(analistaFiltro);
		return null;
	}
	
	public String limpar() 
	{		
		analistaFiltro = new Analista();		
		return null;
	}
	
	
	public String incluir(){
		setOperacao(OperacaoEnum.INCLUIR);
		analistaForm = new Analista();
		return null;
	}
	
	public String salvar()
	{
		try{
			
			validar();
			analistaDAO.salvarAnalista(this.analistaForm);
			setOperacao(OperacaoEnum.FILTRAR);
			
			//validarAlteracao();
			//TODO: CHAMA DAO
		}catch(Exception e){
			adicionarMensagemAviso("Analista já cadastrado!");
			return null;
			
		}
		
		
		return filtrar();
	}	
	
	private void validar() throws Exception{
		  
		List<Analista> analistas = analistaDAO.recuperarAnalistaPorParametro(this.analistaForm);
		if (analistas.size() > 0){
			throw new Exception("Analista já cadastrado!");
		}
		
	}
	
	public String alterar(Analista analista)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		analistaForm = analista;
		return null;
	}
	
	public String excluir(Analista analista)
	{
		return null;
	}			

	public List<Analista> getAnalistas() {	
		return this.analistas;
	}
	
	
	public String visualizarAnalista(Analista analista)
	{
		return null;
	}
	
	public String alterarAnalista(Analista analista)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		analistaForm = analista;
		return null;
	}
	
	public String excluirAnalista(Analista analista)
	{	
		try{
			analistaDAO.excluirAnalista(analista);
		}catch(Exception e){
			e.printStackTrace();
			adicionarMensagemErro("DEU MERDA");
		}
		return filtrar();
	}
	
	
	public Analista getAnalistaFiltro()
	{
		return this.analistaFiltro;
	}
	
	public Analista getAnalistaForm()
	{
		return this.analistaForm;
	}
	
	public void setAnalistaFiltro(Analista analista)
	{
		this.analistaFiltro = analista;
	}
	
	public void setAnalistaForm(Analista analista)
	{
		this.analistaForm = analista;
	}

}
