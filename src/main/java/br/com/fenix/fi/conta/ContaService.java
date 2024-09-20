package br.com.fenix.fi.conta;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.IServico;
import br.com.fenix.abstrato.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import lombok.AllArgsConstructor;

@Service
public class ContaService  extends ServicoAbstrato<ContaRepositorio,Conta,Long> implements IServico<Conta,Long> {


	
	public ContaService(ContaRepositorio repositorio) {
		super(repositorio);
	}

	@Override
	public Conta antesDeSalvar(Conta entidade) throws NegocioException {
//		System.out.println("Valida conta");
//		Optional<Conta> contaApelido =    repositorio.findByApelido(entidade.getApelido()); 
//
//		if (contaApelido.isPresent() && entidade.isNew() ) { 
//		
//			throw new NegocioException("Conta/Cartão já cadastrada com esse apelido");
//		}	
		return entidade;
	}

	@Override
	public Conta antesDeAlterar(Conta entidade) throws NegocioException {
		System.out.println("antesDeAlterar");
//		Optional<Conta> contaApelido =    repositorio.findByApelido(entidade.getApelido()); 
//		if (contaApelido.get().getId() != entidade.getId()) { 		
//			throw new NegocioException("Conta/Cartão não pode ser alterada para esse apelido");
//		}	
		return entidade;
	}

	@Override
	public void depoisDeAlterar(Conta entidade) throws NegocioException {
		
	}

	@Override
	public void depoisDeSalvar(Conta entidade) throws NegocioException {
	
	}

	@Override
	public void antesDeExcluir(Long id) throws NegocioException {
//		System.out.println("Antes de Excluir ->  Conta");
//		boolean existeLancamento = repositorio.existsByContaLancamento (id);
//		if (existeLancamento) {
//			throw new NegocioException("Conta/Cartão possui lançamento e não pode ser excluida");
//		}
	}

	@Override
	public void handleException(OperacaoDB op,Exception e) throws Exception {
		  if ( e instanceof 	ConstraintViolationException) { 
			  throw new NegocioException("Já ha um conta/cartão com esse apelido");
		  }
		  if ( e instanceof 	DataIntegrityViolationException) { 
			  if (op == OperacaoDB.UPT) { 
				  throw new NegocioException("Já ha um conta/cartão com esse apelido");				  
			  }
			  throw new NegocioException("Conta/Cartão possui lançamento e não pode ser excluida");
		  }
		  
	
 	  throw e ;		
	}

	
}
