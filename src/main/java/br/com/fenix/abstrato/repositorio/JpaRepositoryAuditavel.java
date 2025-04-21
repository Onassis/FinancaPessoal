package br.com.fenix.abstrato.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;

import br.com.fenix.abstrato.base.EntidadeAuditavel;

@NoRepositoryBean
public interface JpaRepositoryAuditavel<T extends EntidadeAuditavel<ID>, ID>  extends JpaRepository<T, ID> {
	

	@Override
    @Query("select p from #{#entityName}  p where p.criadoPor.id = ?#{ principal.id}")
	List<T> findAll();

	@Override
	@Query("select p from #{#entityName}  p where p.id = ?1 and p.criadoPor.id = ?#{ principal.id}")
	Optional<T> findById (ID id);

	@Override
	@Query("SELECT (count(p) > 0) FROM #{#entityName} p WHERE p.id = ?1 AND p.criadoPor.id = ?#{principal.id}")
	boolean existsById(ID id);
	
	 @Modifying 
	@Query("delete from #{#entityName}  p where p.id = ?1 and p.criadoPor.id = ?#{ principal.id}")
	void deleteById(ID id);
	
	 @Modifying 
	 @Query("delete from #{#entityName}  p where p.criadoPor.id = ?#{ principal.id}")
	void deleteAll();
	
//	 @PreAuthorize("#criadoPor.id == principal.id") 
//	 <S extends T> S save(S entity);
//	 
//	 @Modifying
//	 @PreAuthorize("s.criadoPor.id == principal.id") 	
//	<S extends T> S saveAndFlush(S entity);
	 
	 
	
}
