package br.com.sgpf.core.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;



/**
 * Bean responsável pela autenticação do usuário.
 * 
 *
 */
@Named("autenticacao")
@SessionScoped
public class AutenticacaoWebBean extends AbstractWebBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// URL a ser invocada ap�s o login, caso a URL desejada n�o exista.
	private static final String URL_AFTER_LOGIN = "/pages/home/home.jsf";

	// URL a ser invocada ap�s o logout
	private static final String URL_AFTER_LOGOUT = "/pages/home/home.jsf";
	
	@Inject
	private Credencial credencial;

	// Marcado para usu�rio j� logado.
	private boolean loggedIn = false;

	// Armazena a URL original, antes do pedido de login.
	private String originalURL;

	public AutenticacaoWebBean() {
	}

	/**
	 * Evento invocado antes de renderizar a tela.
	 * 
	 * @throws IOException
	 */
	public void preRenderView() {

		armazenarURLOriginal();

	}

	private void armazenarURLOriginal() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

		if (originalURL != null) {

			String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				originalURL += "?" + originalQuery;
			}

		}

	}


	public void login() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		

		try {
			
		
			
			loggedIn = true;							
						
			if (originalURL == null) {
				originalURL = externalContext.getRequestContextPath() + URL_AFTER_LOGIN;
			}

			externalContext.redirect(originalURL);

		} catch (Exception e) {
			context.addMessage(null, new FacesMessage("Erro ao realizar autenticação ou usuário não tem permissão de acesso"));
			e.printStackTrace();
		}

	}


	public void logout() throws IOException {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();

		externalContext.redirect(externalContext.getRequestContextPath() + URL_AFTER_LOGOUT);

	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

}