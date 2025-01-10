package br.com.fenix.abstrato.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.abstrato.base.EntidadeAuditavel;

@NoRepositoryBean
public interface CrudRepositoryAuditavel<T extends EntidadeAuditavel<ID>, ID>  extends JpaRepository<T, ID> {
	
	//@Query("select p from #{#entityName} p where ?1 member of p.categories")
	@Override
    @Query("select p from #{#T} p where p.criadoPor.id = ?#{ principal.id}")
	List<T> findAll();
	
}
