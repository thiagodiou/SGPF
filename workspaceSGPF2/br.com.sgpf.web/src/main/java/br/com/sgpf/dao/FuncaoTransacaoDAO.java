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
import br.com.sgpf.filtro.FuncaoTransacaoFiltro;
import br.com.sgpf.model.FuncaoDadosProjeto;
import br.com.sgpf.model.FuncaoTransacaoProjeto;


public class FuncaoTransacaoDAO {
	private EntityManager em = JPAUtil.getEntityManger();

	public void salvar(FuncaoTransacaoProjeto funcaoTransacao) throws Exception
	{	
		
		try{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();		
			//em.clear();
			em.setFlushMode(FlushModeType.COMMIT);
				
			if(funcaoTransacao.getTiposDeDados() != null){
				funcaoTransacao.getTiposDeDados().forEach(t -> {					
					//t.setFuncaoTransacao(funcaoTransacao);
					
					//t = em.merge(t);
					System.out.println(em.contains(t));
				});	
			}	
					
			System.out.println("Salvando a função: " + funcaoTransacao.getNome());									
			em.merge(funcaoTransacao);									
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FuncaoTransacaoProjeto> recuperarFuncaoDadosPorParametro(FuncaoTransacaoFiltro filtro)
	{
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder hql = new StringBuilder();
		
		hql.append("select ftp from FuncaoTransacaoProjeto ftp where 1=1 ");
		
		if(filtro.getProjetoSelecionado() != null){
			hql.append(" and ftp.projeto.id =:idProjeto ");
			parameters.put("idProjeto", filtro.getProjetoSelecionado().getId());				
		}
		
		if(filtro.getEmpresaSelecionada() != null){
			hql.append(" and ftp.projeto.sistema.empresa.id =:idEmpresa ");
			parameters.put("idEmpresa", filtro.getEmpresaSelecionada().getId());
		}
		
		if(filtro.getSistema() != null){
			hql.append(" and ftp.projeto.sistema.id =:idSistema ");
			parameters.put("idSistema", filtro.getSistema().getId());
		}
		
		if(filtro.getNomeFuncaoTransacao() != null && !filtro.getNomeFuncaoTransacao().isEmpty()){	
			hql.append(" and upper(ftp.nome) like upper(:nomeFuncao) ");
			parameters.put("nomeFuncao", filtro.getNomeFuncaoTransacao());
		}
		
		Query q = em.createQuery(hql.toString());
		
		Iterator<Entry<String, Object>> iteratorPar = parameters.entrySet().iterator();		
		while(iteratorPar.hasNext()){
			Map.Entry pair = (Map.Entry) iteratorPar.next(); 										
			q.setParameter(pair.getKey().toString(), pair.getValue());
		}
		
		return q.getResultList();
		
	}		
	
	public List<FuncaoTransacaoProjeto> buscarPorNomeExatoEProjeto(Long idProjeto, String nome){
		String hql = "select ftp from FuncaoTransacaoProjeto ftp where ftp.projeto.id =:idProjeto and upper(ftp.nome) = upper(:nome)";
		
		Query q = em.createQuery(hql).setParameter("nome", nome).setParameter("idProjeto", idProjeto);
		
		return q.getResultList();
	}
		
	
	public List<FuncaoTransacaoProjeto> buscarBaseLinePorSistema(Long idSistema){
		
		String hql = "select ftp from FuncaoTransacaoProjeto ftp where ftp.perteceBaseline =:flagPertence and ftp.projeto.sistema.id =:idSistema";
		
		Query q = em.createQuery(hql).setParameter("flagPertence", SimNaoEnum.SIM)
				.setParameter("idSistema", idSistema);
		
		
		return q.getResultList();
	}
	
	public void excluir(FuncaoTransacaoProjeto ftp){	
		//em.clear();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		ftp.getTiposDeDados().forEach(t -> {
			t.setFuncaoTransacao(null);
			em.merge(t);
		});
		ftp = em.merge(ftp);
		em.remove(ftp);
		tx.commit();
			
	}
	
	public void atualizarBaselineAnterior(Long idSistema){
		EntityTransaction tx = em.getTransaction();
		tx.begin();					
		List<FuncaoTransacaoProjeto> baseline = buscarBaseLinePorSistema(idSistema);
		baseline.forEach(b-> {
			b.setPerteceBaseline(SimNaoEnum.NAO);
			em.merge(b);
		});
		
		tx.commit();
	}
	
}
