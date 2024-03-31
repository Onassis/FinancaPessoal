package br.com.fenix.abstrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.seguranca.modelo.Usuario;

@NoRepositoryBean
public interface GenericRepositoryAutenticado<T> extends GenericRepository<T>  {
	
	public Iterable<T> findByCriadoPor(Usuario usuario);

}
