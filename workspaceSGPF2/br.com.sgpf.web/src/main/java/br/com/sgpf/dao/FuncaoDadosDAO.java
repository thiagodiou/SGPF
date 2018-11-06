package br.com.sgpf.dao;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import br.com.sgpf.enumerator.SimNaoEnum;
import br.com.sgpf.filtro.FuncaoDadosFiltro;
import br.com.sgpf.model.FuncaoDadosProjeto;


public class FuncaoDadosDAO {
	private EntityManager em = JPAUtil.getEntityManger();

	public void salvarFuncaoDados(FuncaoDadosProjeto funcaoDados) throws Exception
	{	
		
		try{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();		
			em.clear();
			em.setFlushMode(FlushModeType.COMMIT);
				
			if(funcaoDados.getTiposDeRegistros() != null){
				funcaoDados.getTiposDeRegistros().forEach(t -> {
					t.setFuncaoDadosProjeto(funcaoDados);
					if(t.getDados() != null)
						t.getDados().forEach(d -> d.setTipoRegistro(t));								
				});	
			}	
					
			System.out.println("Salvando a função: " + funcaoDados.getNome());
			if(funcaoDados.getId() == null){								
				em.persist(funcaoDados);
			}
				
			else
				em.merge(funcaoDados); 
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
			throw e;
		}
	}
	
	
	public void removeDado(Long id){
		String sql = "delete from Dado d where d.id =:id";		
		Query q = em.createQuery(sql).setParameter("id", id);		
		q.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public FuncaoDadosProjeto buscarPorId(Long id){
		
		String hql = "select fdp from FuncaoDadosProjeto fdp where fdp.id =:id";
		
		Query q = em.createQuery(hql, FuncaoDadosProjeto.class).setParameter("id", id);
		FuncaoDadosProjeto retorno = null;
		
		List<FuncaoDadosProjeto> funcoesDados = q.getResultList();		
		
		return funcoesDados.isEmpty() ? null : funcoesDados.get(0);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FuncaoDadosProjeto> listarFuncaoDados()
	{
		Query q =  em.createQuery("select fdp from FuncaoDadosProjeto fdp", FuncaoDadosProjeto.class);
		//q.setParameter("nomeAnalista", nome);	
		List<FuncaoDadosProjeto> funcoesDados = q.getResultList();		
		
		return funcoesDados;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<FuncaoDadosProjeto> recuperarFuncaoDadosPorParametro(FuncaoDadosFiltro filtro)
	{
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder hql = new StringBuilder();
		
		hql.append("select fdp from FuncaoDadosProjeto fdp where 1=1 ");
		
		if(filtro.getProjetoSelecionado() != null){
			hql.append(" and fdp.projeto.id =:idProjeto ");
			parameters.put("idProjeto", filtro.getProjetoSelecionado().getId());				
		}
		
		if(filtro.getEmpresaSelecionada() != null){
			hql.append(" and fdp.projeto.sistema.empresa.id =:idEmpresa ");
			parameters.put("idEmpresa", filtro.getEmpresaSelecionada().getId());
		}
		
		if(filtro.getSistema() != null){
			hql.append(" and fdp.projeto.sistema.id =:idSistema ");
			parameters.put("idSistema", filtro.getSistema().getId());
		}
		
		if(filtro.getNomeFuncaoDados() != null && !filtro.getNomeFuncaoDados().isEmpty()){	
			hql.append(" and upper(fdp.nome) like upper(:nomeFuncao) ");
			parameters.put("nomeFuncao", filtro.getNomeFuncaoDados());
		}
		
		Query q = em.createQuery(hql.toString());
		
		Iterator<Entry<String, Object>> iteratorPar = parameters.entrySet().iterator();		
		while(iteratorPar.hasNext()){
			Map.Entry pair = (Map.Entry) iteratorPar.next(); 										
			q.setParameter(pair.getKey().toString(), pair.getValue());
		}
		
		return q.getResultList();
		
	}		
	
	public List<FuncaoDadosProjeto> buscarPorNomeExatoEProjeto(Long idProjeto, String nome){
		String hql = "select fdp from FuncaoDadosProjeto fdp where fdp.projeto.id =:idProjeto and upper(fdp.nome) = upper(:nome)";
		
		Query q = em.createQuery(hql).setParameter("nome", nome).setParameter("idProjeto", idProjeto);
		
		return q.getResultList();
	}
	
	public List<FuncaoDadosProjeto> buscarBaseLinePorSistema(Long idSistema){
		
		String hql = "select fdp from FuncaoDadosProjeto fdp where fdp.perteceBaseline =:flagPertence and fdp.projeto.sistema.id =:idSistema";
		
		Query q = em.createQuery(hql).setParameter("flagPertence", SimNaoEnum.SIM)
				.setParameter("idSistema", idSistema);
		
		
		return q.getResultList();
	}
	
	public void excluirFuncaoDados(FuncaoDadosProjeto fdp){		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		fdp = em.merge(fdp);
		em.remove(fdp);
		tx.commit();
			
	}
	
	public void atualizarBaselineAnterior(Long idSistema){
		EntityTransaction tx = em.getTransaction();
		tx.begin();					
		List<FuncaoDadosProjeto> baseline = buscarBaseLinePorSistema(idSistema);
		baseline.forEach(b-> {
			b.setPerteceBaseline(SimNaoEnum.NAO);
			em.merge(b);
		});
		
		tx.commit();
	}
}
