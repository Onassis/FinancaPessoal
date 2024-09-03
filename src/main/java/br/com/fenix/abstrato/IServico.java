package br.com.fenix.abstrato;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;

/*
 * Interface de Servico
 */

public interface IServico<T,ID> {

	 Optional<T>  buscarPorId(ID id) throws RegistroNaoExisteException;
	 Iterable<T> listar() throws RegistroNaoExisteException;
	 Page<T> listarPagina(Pageable pageable);
	 
	 T criar(T entidade) throws NegocioException;
	 T atualizar(T entidade) throws NegocioException;
	 void excluirPorId(ID id) throws NegocioException;
	 void excluirTodos();
	 
	 void antesDeSalvar(T entidade)  throws NegocioException;
	 void depoisDeSalvar(T entidade)  throws NegocioException;
	 
	 void antesDeAlterar(T entidade)  throws NegocioException;
	 void depoisDeAlterar(T entidade)  throws NegocioException;

	 void antesDeExcluir(ID id)  throws NegocioException;
}
