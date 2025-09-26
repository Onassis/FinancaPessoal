package br.com.fenix.fi.detalheLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.view.ITotalMesDetalhe;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface DetalheLancamentoRepositorio extends JpaRepositoryAuditavel<DetalheLancamento,UUID> {
	
	
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento where d.id = ?1 and d.criadoPor.id = ?#{ principal.id}")
	Optional<DetalheLancamento> findById (Long id);

	@Query("from DetalheLancamento d JOIN FETCH d.lancamento where  "
			+ " d.criadoPor.id = ?#{ principal.id} "
			+ " and d.contaLancamento = :conta "
			+ " and d.dataRef between :dataInicio and :dataFim ")		
	public List<DetalheLancamento> findByContaAndDataRefBetween (@Param("conta") Conta conta, @Param("dataInicio")  LocalDate dataInicio, @Param("dataFim") LocalDate dataFim); 

	/**
	 * SELECT * FROM sua_tabela WHERE data_criacao >= NOW() - INTERVAL '24 hours' 
	 * ORDER BY data_criacao DESC;
	 * SELECT * FROM sua_tabela ORDER BY id DESC LIMIT 1;
	 */
	
/**
 * Consulta lancamentos em periodo   
 * @param dataInicio
 * @param dataFim
 * @return
 */
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento where d.dataRef between :dataInicio and :dataFim   and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	List <DetalheLancamento> findAllBydataVenctoBetween( @Param("dataInicio")  LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

	@Query("SELECT p FROM DetalheLancamento p ORDER BY p.dtCriacao DESC")
	List<DetalheLancamento> findUltimos10();
	
//	@Query("from DetalheLancamento l where l.dataCompensacao is null and l.criadoPor.id = ?#{ principal.id} order by l.dataLancamento ")
//	List <DetalheLancamento> findAllBydataCompensacaoIsNull();
	
/**
 * 	
 * @param conta
 * @param dataIni
 * @param chaveBanco
 * @return
 */
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento where  "
			+ " d.criadoPor.id = ?#{ principal.id} "
			+ " and d.contaLancamento = :conta "
			+ " and d.dataVenc = :data "
			+ " and d.chaveBanco = :chaveBanco ")
	public Optional<DetalheLancamento> findByContaAndChaveBancoData (@Param("conta") Conta conta,  @Param("data") LocalDate dataIni, @Param("chaveBanco") String chaveBanco ); 
	
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento l where d.contaLancamento = ?1 and  d.dataVenc >= ?2 and"
			+ " d.valor = ?3 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	public List<DetalheLancamento> findbyContaAndByDataVencandByValor(Conta conta, LocalDate dataVenc, BigDecimal valor); 	
	
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento l where d.contaLancamento = null " + 
	        " and  d.dataVenc between ?1 and ?2 " + 
	        " and d.valor = ?3 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	public List<DetalheLancamento> findbyDtVencBetweenAndByValor(LocalDate dataInicio, LocalDate dataFim, BigDecimal valor); 	
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.dataVenc >= ?2 and "
			+ "d.criadoPor.id = ?#{ principal.id} " )		
	double TotalConta(Conta conta, LocalDate data);
	
//	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
//			+ "where d.contaLancamento = ?1 and  d.dataVenc  between ?2 and ?3  "
//			+ "d.criadoPor.id = ?#{ principal.id} " )		
//	double TotalContaPeriodo(Conta conta, LocalDate dataIni, LocalDate dataFim);
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.ano = ?2 and d.mes = ?3 and "
			+ "d.criadoPor.id = ?#{ principal.id} and " 
			+ "d.conciliado = true " )		
	double TotalMesConta(Conta conta, Integer ano, Integer mes);
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.dataVenc between ?2 and ?3 and "
			+ "d.criadoPor.id = ?#{ principal.id}  and " 
			+ "d.conciliado = true " )				
	double TotalByStartDataBetween (Conta conta, LocalDate dataIni, LocalDate dataFim);
	
	@Query(value = 
			"SELECT d.conta_lancamento_id as conta,"
			+ "to_date(to_char(d.dataVenc , 'YYYYMM') || '01', 'YYYYMMDD') AS data, sum(d.valor) AS valor "
			+ "	FROM detalhe_lancamento d "
			+ " WHERE d.conta_lancamento_id = :conta  and d.dataVenc >= :data" 
			+ " GROUP BY conta, data" , nativeQuery = true)
   	List<ITotalMesDetalhe> findByContaGeData (Long conta,LocalDate data);
	
	  @Transactional(propagation = Propagation.MANDATORY) // Operações de modificação devem ser transacionais.
	  @Modifying
	  @Query("UPDATE DetalheLancamento d " +
	         "SET d.chaveBanco =  :chaveBanco, " +
			 " d.dataPgto = :data, " +
			 " d.valorPgto = :valorPgto, " +	  
			 " d.alteradoPor.id = ?#{ principal.id}, " +
			 " d.dtAlteracao  = :dataAtual , " + 
			 " d.conciliado = true " + 	         
	         "WHERE d.id = :detalheId" )
	  int atualizaConciliadacao( @Param("detalheId") UUID detalheId, 
			  					 @Param("chaveBanco") String chaveBanco,
	          					 @Param("data") LocalDate data, 
	          					 @Param("dataAtual") LocalDateTime dataAtual, 
	          					 @Param("valorPago") BigDecimal valorPago);
	
/*	@Query("select COALESCE(sum(l.valor),0) DetalheLancamento l "
			+ "where l.contaLancamento = ?1 and  l.dataPgto >= ?2 and "
			+ "l.dataPgto < ?3 and l.criadoPor.id = ?#{ principal.id} ")
	double totalLancamento(Conta conta, LocalDate saldoAnterio, LocalDate saldoAtual);
*/	
}
