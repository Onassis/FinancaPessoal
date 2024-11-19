package br.com.fenix.fi.automacao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.IServico;
import br.com.fenix.abstrato.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.modelo.LancAux;

@Service
public class AutomacaoServico  extends ServicoAbstrato<AutomacaoRepositorio,Automacao,Long> implements IServico<Automacao,Long> {

	Iterable <Automacao> automacoes; 
	
	
	public AutomacaoServico(AutomacaoRepositorio repositorio) {
		super(repositorio);
	}
	

	public void automatizar ( LancAux lancDTO) {  
		automacoes = repositorio.findAll();		
		for (Automacao auto  : automacoes) {
			for ( String criterio : StringUtils.split(auto.getCriterio(),";") ) {				
				if (lancDTO.criterio(criterio)) { 
					System.out.println(auto);
					lancDTO.setLancamentoFavorecido(  auto.getFavorecido());
					lancDTO.setLancamentoSubCategoria(auto.getSubCategoria());
					lancDTO.setContaDestino(auto.getContaTransferencia());
					if (lancDTO.isContaCorrente()) { 
						lancDTO.setLancamentoTipoOperacao( auto.getTipoOperacao());
					}	
					break;
				}
			}
		}
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
