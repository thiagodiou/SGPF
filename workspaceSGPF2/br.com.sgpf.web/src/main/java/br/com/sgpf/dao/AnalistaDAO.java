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

//import br.com.sgpf.core.bean.OperacaoEnum;
import br.com.sgpf.model.Analista;



public class AnalistaDAO {
	private EntityManager em = JPAUtil.getEntityManger();

	public void salvarAnalista(Analista analista) throws Exception
	{	
		System.out.println("Salvando o analista: " + analista.getNome());
		em.getTransaction().begin();
		em.persist(analista);
		em.getTransaction().commit();
		//em.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Analista> recuperarAnalistaPorParametro(Analista filtro)
	{
		//Query q =  em.createQuery("select a from Analista a where a.nome= :nomeAnalista", Analista.class);
		
		String hql = "select analista from Analista analista where 1=1 ";
	
		Map<String , Object> parameters = new HashMap<String, Object>();
		
		if(filtro.getNome() != null && !filtro.getNome().isEmpty()){
			hql = hql + " and lower(analista.nome) like :nomeAnalista";
		
			parameters.put("nomeAnalista", "%" + filtro.getNome().toLowerCase() + "%");	
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
	public List<Analista> listarAnalistas()
	{
		Query q =  em.createQuery("select a from Analista a", Analista.class);
		//q.setParameter("nomeAnalista", nome);	
		List<Analista> analistas = q.getResultList();		
		
		return analistas;
		
	}
	
	
	public void excluirAnalista(Analista analista)
	{
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		analista = em.merge(analista);
		em.remove(analista);
		tx.commit();
		//em.close();
		
	}
	
}
