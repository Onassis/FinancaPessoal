package br.com.fenix.fi.automacao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.fi.favorecido.Favorecido;


@Repository
public interface AutomacaoRepositorio extends GenericRepository<Automacao> {
	
	@Override
	@Query("from Automacao f where f.criadoPor.id = ?#{ principal.id} order by f.ordem")
	public List<Automacao> findAll();
	
}
