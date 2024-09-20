package br.com.fenix.abstrato;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import jakarta.persistence.EntityTransaction;

/*
 * Interface de Servico
 */

public interface IServico<T,ID> {
	
	 EntityTransaction geradorTransacao(); 
	 
	 Optional<T>  buscarPorId(ID id) throws RegistroNaoExisteException;
	 Iterable<T> listar() throws RegistroNaoExisteException;
	 Page<T> listarPagina(Pageable pageable);
	 void handleException(OperacaoDB op,Exception e) throws Exception;
	 T criar(T entidade) throws Exception;
	 T atualizar(T entidade) throws Exception;
	 void excluirPorId(ID id) throws Exception;
	 void excluirTodos();
	 
	 T antesDeSalvar(T entidade)  throws NegocioException;
	 void depoisDeSalvar(T entidade)  throws NegocioException;
	 
	 T antesDeAlterar(T entidade)  throws NegocioException;
	 void depoisDeAlterar(T entidade)  throws NegocioException;

	 void antesDeExcluir(ID id)  throws NegocioException;
}
