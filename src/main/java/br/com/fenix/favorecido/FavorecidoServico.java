package br.com.fenix.favorecido;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.IServico;
import br.com.fenix.abstrato.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.enumerado.OperacaoDB;

@Service
public class FavorecidoServico  extends ServicoAbstrato<FavorecidoRepositorio,Favorecido,Long> implements IServico<Favorecido,Long> {


	public FavorecidoServico(FavorecidoRepositorio repositorio) {
		super(repositorio);

	}

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
	
		  if ( e instanceof 	DataIntegrityViolationException) { 
			  throw new NegocioException("Favorecido possui lançamento e não pode ser excluida");
		  }		  
	
 	  throw e ;	
	}

	@Override
	public Favorecido antesDeSalvar(Favorecido entidade) throws NegocioException {
		return entidade;
	}

	@Override
	public void depoisDeSalvar(Favorecido entidade) throws NegocioException {

	}

	@Override
	public Favorecido antesDeAlterar(Favorecido entidade) throws NegocioException {
		return entidade;
	}

	@Override
	public void depoisDeAlterar(Favorecido entidade) throws NegocioException {

	}

	@Override
	public void antesDeExcluir(Long id) throws NegocioException {

	}
}