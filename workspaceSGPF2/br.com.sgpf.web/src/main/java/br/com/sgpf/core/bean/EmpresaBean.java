package br.com.sgpf.core.bean;


import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.ViewScoped;

import br.com.sgpf.dao.EmpresaDAO;
import br.com.sgpf.model.Empresa;




@Named
@ViewScoped
public class EmpresaBean extends AbstractWebBean {

	private static final long serialVersionUID = 5653645873997247063L;

	protected static Logger logger = Logger.getLogger(EmpresaBean.class);
		
	private List<Empresa> empresas;	
		
	private Empresa empresaForm;
	
	private Empresa empresaFiltro;
	
	private EmpresaDAO empresaDAO = new EmpresaDAO();
		
	
	public void preRenderView() 
	{		
		if (!FacesContext.getCurrentInstance().isPostback()) 
		{			
			empresaFiltro = new Empresa();
			setOperacao(OperacaoEnum.FILTRAR);			
			this.empresas =  empresaDAO.listarEmpresas();
		}
	}
	
	
	public String filtrar() 
	{
		setOperacao(OperacaoEnum.FILTRAR);	
		this.empresas =  empresaDAO.recuperarEmpresaPorParametro(empresaFiltro);
		return null;
	}
	
	public String limpar() 
	{		
		empresaFiltro = new Empresa();		
		return null;
	}
	
	
	public String incluir(){
		setOperacao(OperacaoEnum.INCLUIR);
		empresaForm = new Empresa();
		return null;
	}
	
	public String salvar()
	{
		try{
			
			validar();
			empresaDAO.salvarEmpresa(this.empresaForm);
			setOperacao(OperacaoEnum.FILTRAR);
			
			//validarAlteracao();
			//TODO: CHAMA DAO
		}catch(Exception e){
			adicionarMensagemAviso(e.getMessage());
			return null;
			
		}
		
		
		return filtrar();
	}	
	
	private void validar() throws Exception{
		  
		if (getOperacao() == OperacaoEnum.INCLUIR){
			List<Empresa> empresas = empresaDAO.listarEmpresas();
			
			for(Empresa empresa: empresas){
				if (empresa.getCnpj().equalsIgnoreCase(empresaForm.getCnpj()) || empresa.getRazaoSocial().equalsIgnoreCase(empresaForm.getRazaoSocial()) || empresa.getNomeFantasia().equalsIgnoreCase(empresaForm.getNomeFantasia()))
				{
					throw new Exception("Empresa já cadastrada!");
				} 
			
			}
		}
		else{
			List<Empresa> empresas = empresaDAO.listarEmpresas();
			for (Empresa empresa : empresas)
			{
				if(this.empresaForm.equals(empresa)){
					continue;
				}
				else
				{
					if (empresa.getCnpj().equalsIgnoreCase(empresaForm.getCnpj()) ){
						throw new Exception ("Já existe empresa cadastrada com esse CNPJ");
					}
						
					if (empresa.getRazaoSocial().equalsIgnoreCase(empresaForm.getRazaoSocial()))
					{
						throw new Exception ("Já existe empresa cadastrada com essa Razão Social");
					}
					
					if (empresa.getNomeFantasia().equalsIgnoreCase(empresaForm.getNomeFantasia())){
						throw new Exception ("Já existe empresa cadastrada com esse Nome Fantasia");
					}
				}
			}
		}
			
		
	}
	
	public String alterar(Empresa empresa)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		empresaForm = empresa;
		return null;
	}
	
	public String excluir(Empresa empresa)
	{
		return null;
	}			

	public List<Empresa> getEmpresas() {	
		return this.empresas;
	}
	
	
	public String visualizarEmpresa(Empresa empresa)
	{
		return null;
	}
	
	public String alterarEmpresa(Empresa empresa)
	{
		setOperacao(OperacaoEnum.ALTERAR);
		empresaForm = empresa;
		return null;
	}
	
	public String excluirEmpresa(Empresa empresa)
	{	
		try{
			empresaDAO.excluirEmpresa(empresa);
		}catch(Exception e){
			e.printStackTrace();
			adicionarMensagemErro("DEU MERDA");
		}
		return filtrar();
	}
	
	
	public Empresa getEmpresaFiltro()
	{
		return this.empresaFiltro;
	}
	
	public Empresa getEmpresaForm()
	{
		return this.empresaForm;
	}
	
	public void setEmpresaFiltro(Empresa empresa)
	{
		this.empresaFiltro = empresa;
	}
	
	public void setEmpresaForm(Empresa empresa)
	{
		this.empresaForm = empresa;
	}

}