package br.com.fenix.abstrato;

import java.util.Collection;
import java.util.List;
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

public interface IServicoDTO<T,DTO, ID> {

	
	 EntityTransaction geradorTransacao(); 
	 

     /*
      * Cria novas intancias das classe pela classe concreta  
      */
	 T criarInstancia();
	 

	 DTO criaDTO();
	 /*
      * Busca no Banco  
      */
	 
	 Optional<T>  buscarPorId(ID id) throws RegistroNaoExisteException;
	 DTO buscaDTOPorId(ID id) throws RegistroNaoExisteException;
	 List<DTO> listar() throws RegistroNaoExisteException;
	 Page<T> listarPagina(Pageable pageable);	 
	 /*
      * Convercação Entidade -> DTO e DTO -> Entidade   
      */	 	 
	 T DTOtoEntidade (DTO dto) throws NegocioException; 
	 T DTOtoEntidade (DTO dto, T entidade) throws NegocioException;
	 DTO EntidadeToDTO(T entidade)  throws RegistroNaoExisteException;

	 /*
      * Operações de Crud no banco    
      */	 	 	 
	 T criar(DTO dto) throws Exception;	 
	 T atualizar(DTO dto) throws Exception;
	 void excluirPorId(ID id) throws Exception;
	 void excluirTodos();
	 /*
      * Tratamento de regras de negocios pela classe concreta     
      */	 	 	 	 
	 T antesDeSalvar(T entidade)  throws NegocioException;
	 T antesDeAlterar(T entidade)  throws NegocioException;

	 void antesDeExcluir(ID id)  throws NegocioException;
	 void depoisDeSalvar(T entidade)  throws NegocioException;
	 
	 
	 /*
      * Tratamento de excetion pela classe concreta     
      */	 	 	 	 
	 
	 void handleException(OperacaoDB op,Exception e) throws Exception;


	

}
