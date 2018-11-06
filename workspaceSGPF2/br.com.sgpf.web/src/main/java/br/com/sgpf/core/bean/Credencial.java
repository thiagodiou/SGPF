package br.com.sgpf.core.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named("credentials")
@SessionScoped
public class Credencial implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	
	private String password;

	public Credencial() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}