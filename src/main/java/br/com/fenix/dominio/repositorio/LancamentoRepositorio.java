package br.com.fenix.dominio.repositorio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;


@Repository
public interface LancamentoRepositorio extends GenericRepository<Lancamento> {
	@Override
	@Query("from Lancamento o where o.criadoPor.id = ?#{ principal.id}")
	public List<Lancamento> findAll(); 


	 
}
