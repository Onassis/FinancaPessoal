package br.com.fenix.fi.detalheLancamento;

import java.io.DataInput;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.*;
import org.modelmapper.PropertyMap;
import org.modelmapper.internal.bytebuddy.build.Plugin.Engine.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.conta.Conta;

import br.com.fenix.fi.lancamento.LancamentoDTO;
import br.com.fenix.fi.saldo.LancamentoAlteradoEvent;
import br.com.fenix.fi.saldo.SaldoConta;
import br.com.fenix.fi.saldo.SaldoContaRepositorio;
import br.com.fenix.fi.saldo.SaldoServico;
import br.com.fenix.fi.upload.LancAux;
import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalheLancServico  extends ServicoAbstratoDTO<DetalheLancamento,LancamentoDTO,UUID> implements IServicoDTO<DetalheLancamento,LancamentoDTO ,UUID> { 
	
	@Autowired
	DetalheLancamentoRepositorio DtlancamentoRP;
	@Autowired
	private ApplicationEventPublisher eventPublisher; // Para publicar eventos

	@Autowired
	SaldoServico saldoSC;

   @Autowired
   private DetalheLancamentoConverter converter;

//   public DetalheLancServico() {
//	   super();
//	}
   
	@Override
	public DetalheLancamentoRepositorio getRp() {	
		return DtlancamentoRP;
	}
	@Override
	public DetalheLancamentoConverter getConverter() {
		return converter;
	}
	
	/**
	 * Retorna o total de lancamentos em um periodo  
	 * @param conta
	 * @param dataIni
	 * @param dataFim
	 * @return
	 */

	
//   public List<LancamentoDTO> findAll () {
//		
//		Iterable<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAll();
//		
//		List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
//		
//		for(DetalheLancamento detLac : detalheLancamentos) {      
//			LancamentoDTO lancamentoDTO = modelMapper.map(detLac, LancamentoDTO.class);
//			lancamentosDTO.add(lancamentoDTO);
//		}	
//		return lancamentosDTO;
//		
//	}	
//	public DetalheLancDTO findDetLanc (Long DetLancId) {
//		
//		Optional<DetalheLancamento>  optDelLac = DtlancamentoRP.findById(DetLancId); 
//		
//		if (optDelLac.isEmpty()) { 
//			return new DetalheLancDTO(); 
//		}
//		DetalheLancamento detLanc = optDelLac.get(); 
//		Lancamento lanc = detLanc.getLancamento(); 
//		DetalheLancDTO lancamentoDTO = modelMapper.map(optDelLac.get(), LancamentoDTO.class); 
////Busca o registro de conta destino 			  
//		if (lanc.isTransferencia()) { 
//			  for(DetalheLancamento lancDest : lanc.getDatalheLancamento())  { 
//				  if (!lancDest.equals(detLanc)) { 
//					  lancamentoDTO.setContaDestino(lancDest.getContaLancamento()) ;						  
//				  }					  
//			  }
//		  }			
//		return lancamentoDTO;		
//	}
//	
	public List<LancamentoDTO> listaPorMesAno (String mesLancamento ) {
	    LocalDate dataInicio=null; 
	    
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		try {
			  dataInicio = LocalDate.parse("01".concat(mesLancamento), formatter);				
		} catch (Exception e) {
			 dataInicio = LocalDate.now();
			 dataInicio = LocalDate.of(dataInicio.getYear(), dataInicio.getMonthValue(), 1);
		}
		  
  	    LocalDate DataFim =  dataInicio.with(TemporalAdjusters.lastDayOfMonth()); 
		
//		 List<DetalheLancamentoConverter> dtos =  DtlancamentoRP
//				 					  .findAllBydataVenctoBetween(dataInicio,DataFim)
//				 					  .stream()
//				 					  .map(dado -> getConverter().convertToDto(dado))
//				 					  .collect(Collectors.toList());
//		 return dtos;
		
return null;
	}
  public LancAux conciliar( LancAux  lancDTO) {
    List<DetalheLancamento> lancamentos ;
    Optional<DetalheLancamento>  lancOpt;
    
    lancamentos  = DtlancamentoRP.
    		findbyContaAndByDataVencandByValor (
    					lancDTO.getContaDestino(), 
    					lancDTO.getDataDoc(), lancDTO.getValor());
    
	for (DetalheLancamento lancDet  : lancamentos  ) {		
			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 			
			lancDTO.setDetalheDestinoId( lancDet.getId());
			lancDTO.setConciliado(true);
			return lancDTO;    			    			
		}
     lancamentos  = DtlancamentoRP.
    		 findbyDtVencBetweenAndByValor (
    				    lancDTO.getDataDoc().minusDays(30),
    					lancDTO.getDataDoc().plusDays(30), lancDTO.getValor());
    
     
     lancOpt = lancamentos.stream()
    		 		.filter(e -> e.getDataVenc().equals(lancDTO.getDataDoc()))
    		 			.findFirst();
     
     if (lancOpt.isPresent()) {
    	    DetalheLancamento detLanc = lancOpt.get(); 
    		lancDTO.setLancamentoId(detLanc.getLancamento().getId()); 			
			lancDTO.setDetalheDestinoId( detLanc.getId());
			lancDTO.setConciliado(true);
			return lancDTO; 
     }
     
     lancOpt = lancamentos.stream()
    		 .filter(e -> e.getValor().equals(lancDTO.getValor()))
				.findFirst();

	if (lancOpt.isPresent()) {
		DetalheLancamento detLanc = lancOpt.get(); 
		lancDTO.setLancamentoId(detLanc.getLancamento().getId()); 			
		lancDTO.setDetalheDestinoId( detLanc.getId());
		lancDTO.setConciliado(true);
		return lancDTO; 
	}
 
 
	return lancDTO;
  }

  @Override
 public void depoisDeSalvar(DetalheLancamento entidade) throws NegocioException { 
	  saldoSC.atualizaSaldo(entidade.getContaLancamento(), entidade.getDataVenc(),entidade.getValor());
	  // Publica o evento para que o listener possa agir	  
//      eventPublisher.publishEvent(new LancamentoAlteradoEvent(entidade.getContaLancamento().getId(), entidade.getDataVenc()));
 }
}
