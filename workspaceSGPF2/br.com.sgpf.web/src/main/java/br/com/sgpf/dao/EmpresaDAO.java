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
import br.com.sgpf.model.Empresa;



public class EmpresaDAO {
	private EntityManager em = JPAUtil.getEntityManger();

	public void salvarEmpresa(Empresa empresa) throws Exception
	{	
		System.out.println("Salvando a empresa: " + empresa.getNomeFantasia());
		em.getTransaction().begin();
		em.persist(empresa);
		em.getTransaction().commit();
		//em.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Empresa> recuperarEmpresaPorParametro(Empresa filtro)
	{
		//Query q =  em.createQuery("select a from Analista a where a.nome= :nomeAnalista", Analista.class);
		
		String hql = "select empresa from Empresa empresa where 1=1 ";
	
		Map<String , Object> parameters = new HashMap<String, Object>();
		
		if(filtro.getNomeFantasia() != null && !filtro.getNomeFantasia().isEmpty()){
			hql = hql + " and lower(empresa.nomeFantasia) like :nomeFantasia";
		
			parameters.put("nomeFantasia", "%" + filtro.getNomeFantasia().toLowerCase() + "%");	
		}
		
		if(filtro.getRazaoSocial() != null && !filtro.getRazaoSocial().isEmpty()){
			hql = hql + " and lower(empresa.razaoSocial) like :razaoSocial";
		
			parameters.put("razaoSocial", "%" + filtro.getRazaoSocial().toLowerCase() + "%");	
		}
		
		if(filtro.getCnpj() != null && !filtro.getCnpj().isEmpty()){
			hql = hql + " and empresa.cnpj like :cnpj";
		
			parameters.put("cnpj",  filtro.getCnpj());	
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
	public List<Empresa> listarEmpresas()
	{
		Query q =  em.createQuery("select e from Empresa e", Empresa.class);
		//q.setParameter("nomeAnalista", nome);	
		List<Empresa> empresas = q.getResultList();		
		
		return empresas;
		
	}
	
	
	public void excluirEmpresa(Empresa empresa)
	{
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		empresa = em.merge(empresa);
		em.remove(empresa);
		tx.commit();
		//em.close();
		
	}
	
}