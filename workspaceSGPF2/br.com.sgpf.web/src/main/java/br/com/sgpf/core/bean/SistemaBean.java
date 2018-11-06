package br.com.sgpf.core.bean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.dao.SistemaDAO;
import br.com.sgpf.model.Empresa;
import br.com.sgpf.model.Sistema;


@Named
@ViewScoped
public class SistemaBean extends AbstractWebBean {

	private static final long serialVersionUID = 5653645873997247046L;

	protected static Logger logger = Logger.getLogger(SistemaBean.class);
		
	private List<Sistema> sistemas;	
		
	private Sistema sistemaForm;
	
	private Sistema sistemaFiltro;
	
	private SistemaDAO sistemaDAO = new SistemaDAO();
	
	private EmpresaDAO empresaDAO = new EmpresaDAO();
	
	private List<Empresa> empresas = empresaDAO.listarEmpresas();
		
	
	public void preRenderView() 
	{		
		if (!FacesContext.getCurrentInstance().isPostback()) 
		{			
			sistemaFiltro = new Sistema();
			setOperacao(OperacaoEnum.FILTRAR);			
			this.sistemas =  sistemaDAO.listarSistemas();
		}
	}
	
	
	public String filtrar() 
	{
		setOperacao(OperacaoEnum.FILTRAR);	
		this.sistemas =  sistemaDAO.recuperarSistemaPorParametro(sistemaFiltro);
		return null;
	}
	
	public String limpar() 
	{		
		sistemaFiltro = new Sistema();		
		return null;
	}
	
	
	public String incluir(){
		setOperacao(OperacaoEnum.INCLUIR);
		sistemaForm = new Sistema();
		return null;
	}
	
	public String salvar()
	{
		try{
			
			//validar();
			sistemaDAO.salvarSistema(this.sistemaForm);
			setOperacao(OperacaoEnum.FILTRAR);
			
			//validarAlteracao();
			//TODO: CHAMA DAO
		}catch(Exception e){
			adicionarMensagemAviso("Sistema já cadastrado!");
			return null;
			
		}
		
		
		return filtrar();
	}		
	
	private void validar() throws Exception{
		  
		if (getOperacao() == OperacaoEnum.INCLUIR){
			List<Sistema> sistemas = sistemaDAO.recuperarSistemaPorParametro(this.sistemaForm);
			if (sistemas.size() > 0)
			{
				throw new Exception("Sistema já cadastrado!");
			}
		}
	}
	
	public String alterar(Sistema sistema)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		sistemaForm = sistema;
		return null;
	}
	
	public String excluir(Sistema sistema)
	{
		return null;
	}			

	public List<Sistema> getSistemas() {	
		return this.sistemas;
	}
	
	
	public String visualizarSistema(Sistema sistema)
	{
		return null;
	}
	
	public String alterarSistema(Sistema sistema)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		sistemaForm = sistema;
		return null;
	}
	
	public String excluirSistema(Sistema sistema)
	{	
		try{
			sistemaDAO.excluirSistema(sistema);
		}catch(Exception e){
			e.printStackTrace();
			adicionarMensagemErro("DEU MERDA");
		}
		return filtrar();
	}
	
	
	public Sistema getSistemaFiltro()
	{
		return this.sistemaFiltro;
	}
	
	public Sistema getSistemaForm()
	{
		return this.sistemaForm;
	}
	
	public void setSistemaFiltro(Sistema sistema)
	{
		this.sistemaFiltro = sistema;
	}
	
	public void setSistemaForm(Sistema sistema)
	{
		this.sistemaForm = sistema;
	}
	
	public List<Empresa> getEmpresas()
	{
		return this.empresas;
	}

}

