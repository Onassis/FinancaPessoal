package br.com.fenix.abstrato.repositorio;

import org.springframework.data.repository.NoRepositoryBean;

import br.com.fenix.seguranca.usuario.Usuario;

@NoRepositoryBean
public interface GenericRepositoryAutenticado<T> extends GenericRepository<T>  {
	
	public Iterable<T> findByCriadoPor(Usuario usuario);

}
