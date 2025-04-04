package br.com.fenix.fi.favorecido;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.fi.conta.Conta;


@Repository
public interface FavorecidoRepositorio extends GenericRepository<Favorecido> {
	
	@Override
	@Query("from Favorecido f where f.criadoPor.id = ?#{ principal.id}")
	List<Favorecido> findAll();
	
	@Query("from Favorecido o where o.id = ?1 and o.criadoPor.id = ?#{ principal.id}")
	Optional<Favorecido> findById (Long id);
	
}
