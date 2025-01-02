package br.com.fenix.fi.automacao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.fi.upload.LancAux;
import br.com.fenix.util.Coletor;
import br.com.fenix.util.Singularizer;
import br.com.fenix.util.TextProcessor;

@Service
public class AutomacaoServico  extends ServicoAbstrato<AutomacaoRepositorio,Automacao,Long> implements IServico<Automacao,Long> {

	Iterable <Automacao> automacoes; 
	Map<Integer , Automacao> automacaoMap =  new HashMap<>();

	public AutomacaoServico(AutomacaoRepositorio repositorio) {
		super(repositorio);
		
	}
	/**
	 	* Função que recebe uma coleção de lancamentos importados do arquivo  e classifica a categoria
	 	* @param Coleção de lancamentos 
	 	* @return void
    */	
	
	public void iniciar() {
		automacoes = repositorio.findAll();
		String normalizedTexto;
		for (Automacao auto  : automacoes) {
			normalizedTexto = Singularizer.converterParaSingular(auto.getCriterio().toLowerCase());
			normalizedTexto = TextProcessor.normalizaTexto(normalizedTexto); 
		    automacaoMap.put(normalizedTexto.hashCode(), auto);	

			System.out.println("Categoria" + auto.getSubCategoria().getDescricao());
			for (String criterio : auto.getCriterios()) { 
 				System.out.println("criterio" + criterio);
 				criterio = Singularizer.converterParaSingular(criterio);
				normalizedTexto = TextProcessor.normalizaTexto(criterio);
			    automacaoMap.put(normalizedTexto.hashCode(), auto);	
			}			
		}		
	}
	
	public void automatizaLactoHash( Coletor coletorLanc) {
		iniciar();		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			automatizarHash(lancDTO);
		}
	}
	private void setAutomacao (LancAux lancDTO,  Automacao auto) {
		lancDTO.setLancamentoFavorecido(  auto.getFavorecido());
		lancDTO.setLancamentoSubCategoria(auto.getSubCategoria());
		lancDTO.setContaDestino(auto.getContaTransferencia());
		if (lancDTO.isContaCorrente()) { 
			lancDTO.setLancamentoTipoOperacao( auto.getTipoOperacao());
		}
	}

	public void automatizarHash ( LancAux lancDTO) {
		
		 String descricao = Singularizer.converterParaSingular(lancDTO.getLancamentoInformacao());
		 String normalize = TextProcessor.normalizaTexto(descricao);
		 
		 Automacao auto = automacaoMap.get(normalize.hashCode());
		 if (auto != null) {
			 setAutomacao (lancDTO,auto);
			 return;
		 }
		 
		 Map<String, Integer>  criterioLacto = TextProcessor.processText(normalize);
		 
		  for (Map.Entry<String, Integer> entry : criterioLacto.entrySet()) {
			  String key = entry.getKey();
			  Integer hash = entry.getValue();
			    
		      auto = automacaoMap.get(hash);
		      if (auto != null) { 
		    	  setAutomacao (lancDTO,auto);
				break;		
		      }	  
		  }	
	
	}
	
//	public void automatizaLacto( Coletor coletorLanc) {
//	automacoes = repositorio.findAll();		
//
//	for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
//		automatizar(lancDTO);
//	}
//}
	

//	public void automatizar ( LancAux lancDTO) {  
//		for (Automacao auto  : automacoes) {
//			for ( String criterio : StringUtils.split(auto.getCriterio(),";") ) {				
//				if (lancDTO.hasCriterio(criterio)) { 
//					System.out.println(auto);
//					lancDTO.setLancamentoFavorecido(  auto.getFavorecido());
//					lancDTO.setLancamentoSubCategoria(auto.getSubCategoria());
//					lancDTO.setContaDestino(auto.getContaTransferencia());
//					if (lancDTO.isContaCorrente()) { 
//						lancDTO.setLancamentoTipoOperacao( auto.getTipoOperacao());
//					}	
//					break;
//				}
//			}
//		}
//	}
	

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
