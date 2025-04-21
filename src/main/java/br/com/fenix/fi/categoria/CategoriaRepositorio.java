package br.com.fenix.fi.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.subCategoria.SubCategoria;

@Repository
public interface CategoriaRepositorio extends JpaRepositoryAuditavel<Categoria,Long> {
	
	  @Query("select c from Categoria c JOIN FETCH c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } and c.tipoLancamento = ?1 order by c.descricao,s.descricao" )
	  List<Categoria> findByTipoLancamentoOrderByDescricaoAsc(TipoLancamento tipoLancamento);
	 
	  @Query("select c from Categoria c LEFT JOIN FETCH c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } and c.tipoCategoria = ?1 order by c.descricao,s.descricao" )
	  List<Categoria> findByTipoCategoriaOrderByDescricaoAsc(TipoCategoria  tipoCategoria);
	  
	  @Query("select c from Categoria c LEFT JOIN FETCH c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } order by c.descricao,s.descricao" )  	
	  List<Categoria> findByAllOrderByDescricaoAsc();
	  
	  @Query("select s from SubCategoria s where s.criadoPor.id = ?#{ principal?.id } and s.id = ?1" )
	  Optional<SubCategoria> findBySubCategoriaId(Long  id);

//	  @Modifying
//	  @Query("delete from SubCategoria b where b.id= ?1")
//	  void deleteSubCategoria( Long id);
}
