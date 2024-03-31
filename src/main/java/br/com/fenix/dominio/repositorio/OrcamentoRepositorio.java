package br.com.fenix.dominio.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.modelo.Orcamento;

public interface OrcamentoRepositorio   extends GenericRepository<Orcamento>{

	@Override
	@Query("from Orcamento o  right join subCategoria s where o.criadoPor.id = ?#{ principal.id}")
	Iterable<Orcamento> findAll();
	

	@Query("select o, s from Orcamento o  right join fetch subCategoria s where o.ano = ?1 and o.criadoPor.id = ?#{ principal.id}")
	List<Orcamento> findAllbyAno(int ano);
}

