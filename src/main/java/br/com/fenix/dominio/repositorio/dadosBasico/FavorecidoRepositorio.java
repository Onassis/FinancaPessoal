package br.com.fenix.dominio.repositorio.dadosBasico;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;


@Repository
public interface FavorecidoRepositorio extends GenericRepository<Favorecido> {
	
	@Override
	@Query("from Favorecido f where f.criadoPor.id = ?#{ principal.id}")
	public List<Favorecido> findAll();
	
}
