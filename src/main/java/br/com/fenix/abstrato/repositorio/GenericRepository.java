package br.com.fenix.abstrato.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.fi.conta.Conta;
import br.com.fenix.seguranca.usuario.Usuario;

@NoRepositoryBean
public interface GenericRepository<T> extends JpaRepository<T, Long> {
	

  //  @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 and e.criadoPor.id = ?#{ principal.id}")
  //  Optional<T> findByIdLogado (Long id);
    
}
