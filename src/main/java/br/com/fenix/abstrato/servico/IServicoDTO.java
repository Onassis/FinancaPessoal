package br.com.fenix.abstrato.servico;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import jakarta.persistence.EntityTransaction;

/*
 * Interface de Servico
 */

public interface IServicoDTO<T, DTO, ID> extends IServico<T,ID> {
	
	List<DTO> listarDto() ;
//	DTO EntidadeToDTO(T entidade);
//	T   DTOtoEntidade(DTO dto);
//	T DTOtoEntidade(DTO dto, T entidade) throws NegocioException;
	DTO buscaDTOPorId(ID id) ;
	
	void depoisDeAlterar(T entidade, DTO dto)  throws NegocioException;
	
	DTO criarDTO(DTO dto) throws Exception; 
	DTO atualizarDTO(DTO dto) throws Exception;

}
