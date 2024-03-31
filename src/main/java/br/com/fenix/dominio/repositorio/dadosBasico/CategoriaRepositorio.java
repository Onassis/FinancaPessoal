package br.com.fenix.dominio.repositorio.dadosBasico;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;

@Repository
public interface CategoriaRepositorio extends GenericRepository<Categoria> {
	
	
	  

	  @Query("select c from Categoria c JOIN FETCH c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } and c.tipoLancamento = ?1 order by c.descricao,s.descricao" )
	  Iterable<Categoria> findByTipoLancamentoOrderByDescricaoAsc(TipoLancamento tipoLancamento);
	
	  @Query("select c from Categoria c LEFT JOIN FETCH c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } order by c.descricao,s.descricao" )  
	
	  Iterable<Categoria> findByAllOrderByDescricaoAsc();
	  
	  @Query("select s from SubCategoria s where s.criadoPor.id = ?#{ principal?.id } and s.id = ?1" )
	  Optional<SubCategoria> findBySubCategoriaId(Long  id);

	  @Modifying
	  @Query("delete from SubCategoria b where b.id= ?1")
	  void deleteSubCategoria( Long id);
}
