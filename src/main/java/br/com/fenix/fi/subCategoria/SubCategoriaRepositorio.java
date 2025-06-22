package br.com.fenix.fi.subCategoria;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;

@Repository
public interface SubCategoriaRepositorio extends JpaRepositoryAuditavel<SubCategoria,Long> {
	
	  
	  @Cacheable(value = "subCategoria", key = "#id")
	  @Override	   
	  @Query("select s from SubCategoria s  LEFT JOIN FETCH s.categoria where s.criadoPor.id = ?#{ principal?.id } and s.id = ?1" )
	  Optional<SubCategoria> findById(Long  id);
	  
	  @Modifying
	  @Query("delete from SubCategoria b where b.id= ?1")
	  void deleteSubCategoria( Long id);
//	  List<SubCategoria> findByTipoLancamentoOrderByDescricaoAsc(TipoLancamento tipoLancamento);
	  @Query("select new br.com.fenix.dominio.modelo.Option(s.id, c.descricao || '->' ||  s.descricao ) from Categoria c LEFT JOIN  c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } and c.tipoCategoria = ?1 order by c.descricao,s.descricao" )
	  List<Option> findOptionByTipoCategoriaOrderByDescricaoAsc(TipoCategoria  tipoCategoria);
	  @Query("select new br.com.fenix.dominio.modelo.Option(s.id, c.descricao || '->' ||  s.descricao ) from Categoria c LEFT JOIN  c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } and c.tipoLancamento = ?1 order by c.descricao,s.descricao" )
	  List<Option> findOptionByTipoCategoriaOrderByDescricaoAsc(TipoLancamento  tipoLancamento);
	  @Query("select new br.com.fenix.dominio.modelo.Option(s.id, c.descricao || '->' ||  s.descricao ) from Categoria c LEFT JOIN  c.subCategoria s where c.criadoPor.id = ?#{ principal?.id } order by c.descricao,s.descricao" )
	  List<Option> findAllOption();
}
