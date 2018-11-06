package br.com.sgpf.core.bean;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;



public abstract class AbstractWebBean implements Serializable {
	
		
	protected OperacaoEnum operacao;
	
	private static final long serialVersionUID = 1L;

	protected String OUTCOME_DETAIL = "detail";

	protected String OUTCOME_FORM = "form";

	protected String OUTCOME_LISTA = "list";
		
	
	protected void adicionarMensagemInfo(String message, Object... params) {
		Messages.addInfo(null, message, params);
	}

	protected void adicionarMensagemAviso(String message, Object... params) {
		Messages.addWarn(null, message, params);
	}

	protected void adicionarMensagemErro(String message, Object... params) {
		Messages.addError(null, message, params);
	}

	protected static HttpServletResponse getResponse() {
		return Faces.getResponse();
	}

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	
	protected void preRender() {
		
	}

	public OperacaoEnum getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacaoEnum operacao) {
		this.operacao = operacao;
	}
	
	
}