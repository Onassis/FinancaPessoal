package br.com.fenix.abstrato;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.seguranca.modelo.Usuario;

@NoRepositoryBean
public interface GenericRepository<T> extends CrudRepository<T, Long> {
	
	

}
