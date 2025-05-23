package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface LancamentoRepositorio extends JpaRepositoryAuditavel<Lancamento,Long> {
	
	
	@Query("select l, d from Lancamento l inner join DetalheLancamento d on d.lancamento = l.id where d.id = ?1 and d.criadoPor.id = ?#{ principal.id}")
	Optional<Lancamento> findDetalheLancamentoById (Long id);
	 
	@Query("select l, d from Lancamento l inner join DetalheLancamento d on l.id  = d.lancamento where d.dataVenc between ?1 and ?2 and d.criadoPor.id = ?#{ principal.id} order by d.dataVenc ")
	List <Lancamento> findAllBydataVenctoBetween( LocalDate dataInicio, LocalDate dataFim);
	 
}
