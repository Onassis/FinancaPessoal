package br.com.fenix.seguranca.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.seguranca.modelo.Usuario;

@EnableJpaRepositories
@Repository
public interface UsuarioRepositorio extends GenericRepository<Usuario> {
	
	Optional<Usuario> findByEmail(String email);
	   
  //  Optional<Usuario> findByUsernameOrEmail(String username, String email);

//    List<Usuario> findByIdIn(List<Long> userIds);

//    Optional<Usuario> findByUsername(String username);

//    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
