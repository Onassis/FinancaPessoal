package br.com.fenix.abstrato.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.seguranca.usuario.Usuario;

@NoRepositoryBean
public interface GenericRepository<T> extends CrudRepository<T, Long> {
	/*
	 * Retornar lista ao inves de Iterable
	 */
	@Override
	 @Query("select p from #{#entityName} p where ?1 member of p.categories")
	List<T> findAll();
}
