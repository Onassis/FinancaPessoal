package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface LancamentoRepositorio extends JpaRepositoryAuditavel<Lancamento,Long> {
	
	
	@Query("select l, d from Lancamento l join fetch l.detalheLancamento d where d.id = :detalheId and d.criadoPor.id = ?#{ principal.id}")
	Optional<Lancamento> findDetalheLancamentoById (@Param("detalheId") Long id);
	 
	@Query("select distinct l,d from Lancamento l join fetch l.detalheLancamento d  where d.dataVenc between :dataInicio and :dataFim  and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	List <Lancamento> findAllBydataVenctoBetween(@Param("dataInicio") LocalDate dataInicio,@Param("dataFim")  LocalDate dataFim); 
	 
}
// melhor performance
//	@Query("select l from Lancamento l join l.detalheLancamento d join fetch l.detalheLancamento where d.dataVenc between ?1 and ?2 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")

//@Query("select l from Lancamento l left outer join l.detalheLancamento d where d.dataVenc between ?1 and ?2 and d.criadoPor.id = ?#{ principal.id} and l.conta = ?3 order by d.dataVenc ")
//        List <Lancamento> findAllBydataVenctoBetweenAndConta( LocalDate dataInicio, LocalDate dataFim, Conta conta);
//
//@Query("select l from Lancamento l left outer join l.detalheLancamento d where d.dataVenc between ?1 and ?2 and d.criadoPor.id = ?#{ principal.id} and l.conta = ?3 and l.categoria = ?4 order by d.dataVenc ")
//List<Lancamento> findAllBydataVenctoBetweenAndContaAndCategoria(LocalDate dataInicio, LocalDate dataFim, Conta conta,
//		Long categoria);

