package br.com.fenix.fi.subCategoria;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoriaRepositorio extends JpaRepository<SubCategoria,Long> {
	
	  

	  @Override	   
	  @Query("select s from SubCategoria s  LEFT JOIN FETCH s.categoria c where s.criadoPor.id = ?#{ principal?.id } and s.id = ?1" )
	  Optional<SubCategoria> findById(Long  id);
	  
	  @Modifying
	  @Query("delete from SubCategoria b where b.id= ?1")
	  void deleteSubCategoria( Long id);
//	  List<SubCategoria> findByTipoLancamentoOrderByDescricaoAsc(TipoLancamento tipoLancamento);
	
}
