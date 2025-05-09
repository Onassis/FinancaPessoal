package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface LancamentoRepositorio extends JpaRepositoryAuditavel<Lancamento,Long> {
	
	@Override
	@Query("from Lancamento l inner join DetalheLancamento d on d.lancamento = l.id where d.id = ?1 and d.criadoPor.id = ?#{ principal.id}")
	Optional<Lancamento> findById (Long id);
	 
}
