package br.com.sgpf.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


import br.com.sgpf.model.Projeto;

public class ProjetoDAO {

	private EntityManager em = JPAUtil.getEntityManger();

	public void salvarProjeto(Projeto projeto) throws Exception
	{	
		System.out.println("Salvando o projeto: " + projeto.getTitulo());
		em.getTransaction().begin();
		projeto = em.merge(projeto);
		em.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> recuperarProjetoPorParametro(Projeto filtro)
	{
		//Query q =  em.createQuery("select a from Analista a where a.nome= :nomeAnalista", Analista.class);
		
		String hql = "select projeto from Projeto projeto where 1=1 ";
	
		Map<String , Object> parameters = new HashMap<String, Object>();
		
		if(filtro.getTitulo() != null && !filtro.getTitulo().isEmpty()){
			hql = hql + " and lower(projeto.titulo) like :tituloProjeto";
		
			parameters.put("tituloProjeto", "%" + filtro.getTitulo().toLowerCase() + "%");	
		}
		
		if(filtro.getIdentificador() !=null && !filtro.getIdentificador().isEmpty()){
			hql = hql + " and lower(projeto.identificador) like :identificadorProjeto";
		
			parameters.put("identificadorProjeto", "%" + filtro.getIdentificador().toLowerCase() + "%");	
		}
	   
		if(filtro.getAnalista() !=null){
			hql = hql + " and projeto.analista.id = :idAnalista";		
			parameters.put("idAnalista",  filtro.getAnalista().getId());	
		}
		
		if(filtro.getSistema() !=null && filtro.getSistema().getId() != null){
			hql = hql + " and projeto.sistema.id = :idSistema";		
			parameters.put("idSistema",  filtro.getSistema().getId());	
		}
		
		if(filtro.getSistema() !=null && filtro.getSistema().getEmpresa() != null){
			hql = hql + " and projeto.sistema.empresa.id = :idEmpresa";		
			parameters.put("idEmpresa",  filtro.getSistema().getEmpresa().getId());	
		}
			 
				
		Query q1 = em.createQuery(hql);			
		
		Iterator<Entry<String, Object>> iteratorPar = parameters.entrySet().iterator();		
		while(iteratorPar.hasNext()){
			Map.Entry pair = (Map.Entry) iteratorPar.next(); 										
			q1.setParameter(pair.getKey().toString(), pair.getValue());
		}
		
				
		return q1.getResultList();
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Projeto> listarProjetos()
	{
		Query q =  em.createQuery("select p from Projeto p", Projeto.class);
		//q.setParameter("nomeAnalista", nome);	
		List<Projeto> projetos = q.getResultList();		
		
		return projetos;
		
	}
	
	
	public void excluirProjeto(Projeto projeto)
	{
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		projeto = em.merge(projeto);
		em.remove(projeto);
		tx.commit();
			
	}
	
}

