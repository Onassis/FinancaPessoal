package br.com.fenix.dominio.repositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.view.ITotalMesDetalhe;


@Repository
public interface DetalheLancamentoRepositorio extends GenericRepository<DetalheLancamento> {
	
	
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento l where d.id = ?1 and d.criadoPor.id = ?#{ principal.id}")
	Optional<DetalheLancamento> findById (Long id);

	@Query("from DetalheLancamento d JOIN FETCH d.lancamento l where d.dataVenc between ?1 and ?2 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	List <DetalheLancamento> findAllBydataVenctoBetween( LocalDate dataInicio, LocalDate dataFim);

//	@Query("from DetalheLancamento l where l.dataCompensacao is null and l.criadoPor.id = ?#{ principal.id} order by l.dataLancamento ")
//	List <DetalheLancamento> findAllBydataCompensacaoIsNull();
	
	@Query("from DetalheLancamento d JOIN FETCH d.lancamento l where d.contaLancamento = ?1 and  d.dataVenc >= ?2 and"
			+ " d.valor = ?3 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	public List<DetalheLancamento> findbyContaAndByDataVencandByValor(Conta conta, LocalDate dataVenc, BigDecimal valor); 	
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.dataVenc >= ?2 and "
			+ "d.criadoPor.id = ?#{ principal.id} " )		
	double TotalConta(Conta conta, LocalDate data);
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.ano = ?2 and d.mes = ?3 and "
			+ "d.criadoPor.id = ?#{ principal.id} and " 
			+ "d.conciliado = true " )		
	double TotalMesConta(Conta conta, Integer ano, Integer mes);
	
	@Query("select COALESCE(sum(d.valor),0) from DetalheLancamento d "
			+ "where d.contaLancamento = ?1 and  d.dataVenc  between  ?2 and ?3 and "
			+ "d.criadoPor.id = ?#{ principal.id}  and " 
			+ "d.conciliado = true " )				
	double TotalMesConta(Conta conta, LocalDate dataIni, LocalDate dataFim);
	
	@Query(value = 
			"SELECT d.conta_lancamento_id as conta,"
			+ "to_date(to_char(d.dataVenc , 'YYYYMM') || '01', 'YYYYMMDD') AS data, sum(d.valor) AS valor "
			+ "	FROM detalhe_lancamento d "
			+ " WHERE d.conta_lancamento_id = :conta  and d.dataVenc >= :data" 
			+ " GROUP BY conta, data" , nativeQuery = true)
   	List<ITotalMesDetalhe> findByContaGeData (Long conta,LocalDate data);
	
	
	
/*	@Query("select COALESCE(sum(l.valor),0) DetalheLancamento l "
			+ "where l.contaLancamento = ?1 and  l.dataPgto >= ?2 and "
			+ "l.dataPgto < ?3 and l.criadoPor.id = ?#{ principal.id} ")
	double totalLancamento(Conta conta, LocalDate saldoAnterio, LocalDate saldoAtual);
*/	
}
