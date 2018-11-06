package br.com.sgpf.dao;



import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import javax.annotation.Generated;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.sgpf.enumerator.SimNaoEnum;
//import br.com.sgpf.core.bean.OperacaoEnum;
import br.com.sgpf.model.Sistema;



public class SistemaDAO {
	private EntityManager em = JPAUtil.getEntityManger();

	public void salvarSistema(Sistema sistema) throws Exception
	{	
		System.out.println("Salvando o sistema: " + sistema.getNome());
		em.getTransaction().begin();
		em.persist(sistema);
		em.getTransaction().commit();
		//em.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Sistema> recuperarSistemaPorParametro(Sistema filtro)
	{
		//Query q =  em.createQuery("select a from Analista a where a.nome= :nomeAnalista", Analista.class);
		
		String hql = "select sistema from Sistema sistema where 1=1 ";
	
		Map<String , Object> parameters = new HashMap<String, Object>();
		
		
		if(filtro.getSigla() != null && !filtro.getSigla().isEmpty()){
			hql = hql + " and lower(sistema.sigla) like :siglaSistema";
		
			parameters.put("siglaSistema", "%" + filtro.getSigla().toLowerCase() + "%");	
		}
		
		
		if(filtro.getNome() != null && !filtro.getNome().isEmpty()){
			hql = hql + " and lower(sistema.nome) like :nomeSistema";
		
			parameters.put("nomeSistema", "%" + filtro.getNome().toLowerCase() + "%");	
		}
		
		if(filtro.getEmpresa() !=null){
			hql = hql + " and sistema.empresa.id = :empresaId";
			
			parameters.put("empresaId", filtro.getEmpresa().getId());
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
	public List<Sistema> listarSistemas()
	{
		Query q =  em.createQuery("select s from Sistema s", Sistema.class);
		//q.setParameter("nomeAnalista", nome);	
		List<Sistema> sistemas = q.getResultList();		
		
		return sistemas;
		
	}
	
	
	public void excluirSistema(Sistema sistema)
	{
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		sistema = em.merge(sistema);
		em.remove(sistema);
		tx.commit();
		//em.close();
		
	}
	
}


