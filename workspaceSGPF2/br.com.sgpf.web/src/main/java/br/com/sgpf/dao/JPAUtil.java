package br.com.sgpf.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
	
	public static EntityManager getEntityManger()
	{
		return emf.createEntityManager();
	}
}

