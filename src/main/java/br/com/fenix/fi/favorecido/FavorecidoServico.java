package br.com.fenix.fi.favorecido;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.OperacaoDB;

@Service
public class FavorecidoServico  extends ServicoAbstrato<FavorecidoRepositorio,Favorecido,Long> implements IServico<Favorecido,Long> {


	public FavorecidoServico(FavorecidoRepositorio repositorio) {
		super(repositorio);

	}
	public List<Option>  listaDeFavorecido() {
		   List<Option> options = repositorio.findAll()
		   		.stream()    
				.map(favorecido  -> new Option(favorecido.getId(), favorecido.getNome()))
	            .collect(Collectors.toList());
			return options;
			
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
		// TODO Auto-generated method stub
		return entidade;
	}
	@Override
	public void depoisDeSalvar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Favorecido antesDeAlterar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return entidade;
	}
	@Override
	public void depoisDeAlterar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void antesDeExcluir(Long id) throws NegocioException {
		// TODO Auto-generated method stub
		
	}

}