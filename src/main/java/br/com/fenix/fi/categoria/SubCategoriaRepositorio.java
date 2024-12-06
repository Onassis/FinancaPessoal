package br.com.fenix.fi.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoLancamento;

@Repository
public interface SubCategoriaRepositorio extends GenericRepository<SubCategoria> {
	
	  

	  @Override	   
	  @Query("select s from SubCategoria s  LEFT JOIN FETCH s.categoria c where s.criadoPor.id = ?#{ principal?.id } and s.id = ?1" )
	  Optional<SubCategoria> findById(Long  id);
	  
//	  List<SubCategoria> findByTipoLancamentoOrderByDescricaoAsc(TipoLancamento tipoLancamento);
	
}
