package br.com.fenix.abstrato.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import br.com.fenix.abstrato.base.EntidadeAuditavel;

@NoRepositoryBean
public interface ReadOnlyRepositoryAutenticado<T extends EntidadeAuditavel<ID>, ID> extends Repository<T, ID> {
 

    @Query("select p from #{#entityName}  p where p.criadoPor.id = ?#{ principal.id}")
	List<T> findAll(); 
    
    @Query("select p from #{#entityName}  p where p.id = ?1 and p.criadoPor.id = ?#{ principal.id}")
	Optional <T> findById(ID id); 
    
	@Query("SELECT count(p) FROM #{#entityName} p WHERE p.id = ?1 AND p.criadoPor.id = ?#{principal.id}")
	long count() ;
	
	@Query("SELECT (count(p) > 0) FROM #{#entityName} p WHERE p.id = ?1 AND p.criadoPor.id = ?#{principal.id}")
	boolean existsById(ID id);
	
    @Query("select p from #{#entityName}  p where p.criadoPor.id = ?#{ principal.id}")
	List<T> findAll(Sort sort);
    
}