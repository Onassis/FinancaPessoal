package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface LancamentoRepositorio extends JpaRepositoryAuditavel<Lancamento,Long> {
	

	 
}
