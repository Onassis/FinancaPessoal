package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.lancamento.operacao.CompraParceladaConverter;
import br.com.fenix.fi.lancamento.operacao.CreditoConverter;
import br.com.fenix.fi.lancamento.operacao.DebitoConverter;
import br.com.fenix.fi.lancamento.operacao.TransferenciaConverter;


@Component
public class LancamentoConverter implements Converter<Lancamento,LancamentoDTO> {
	 @Autowired
	 ConverterLancamentoFactory converters;
	   
	@Override
	public LancamentoDTO ToDto(Lancamento entity) {
		
		return new LancamentoDTO(entity);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Lancamento ToEntity(LancamentoDTO dto) {
		TipoOperacao  tipoOperacao = dto.getTipoOperacao();
		Converter converter = converters.getConverter(tipoOperacao.toString());
		Lancamento lancamento= (Lancamento) converter.ToEntity(dto);  
//	    switch (tipoOperacao) {
//	    	case CP -> lancamento = new CompraParceladaConverter().ToEntity(dto);
//	    	case CR -> lancamento = new CreditoConverter().ToEntity(dto);
//	    	case DB -> lancamento = new DebitoConverter().ToEntity(dto);
////	    	case EP -> "Empréstimo";
////	    	case ES -> "Estorno";
////	    	case PG -> "Pagamento";
//	    	case TR -> lancamento = new TransferenciaConverter().ToEntity(dto); 
//        }
    	return lancamento; 	
	}
	
//	private Lancamento dtoToLancamento(LancamentoDTO dto  ) {
//		
//		return  new Lancamento().builder()
//	    		.dataDoc(dto.dataDoc) 	    		
//	    		.nroInicialPrestacao(dto.nroInicialPrestacao) 
//	    		.nroPrestacao(dto.nroPrestacao)
//	    		.informacao(dto.getInformacao()) 
//	    		.observacao(dto.getObservacao())
////	    		.categoria(dto.getSubCategoria().getCategoria()) 
//	    		.subCategoria(dto.subCategoria) 
//	    		.favorecido(dto.favorecido)
//	    		.tipoOperacao(dto.tipoOperacao)
//	    		.total(dto.total)
//	    		.build() ;
//	    
//	}
//
//	public Lancamento Transferir (LancamentoDTO dto  ) {
//	
//		Lancamento lancamento = dtoToLancamento(dto);
//  
//		LocalDate data = lancamento.getDataDoc();
//
//		DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
//	 			.prestacao(1)
//	 			.valor(lancamento.getValorPrestacao()) 
//	 			.tipoLancamento(dto.tipoLancamento)
//	 			.dataVenc(data)
//	 			.contaLancamento(dto.contaLancamento)
//	 			.contaTransferencia(dto.contaTransferencia)
//	 			.ano(data.getYear()) 
//	 			.mes(data.getMonthValue())
//	 			.conciliado(dto.conciliado)
//	 			.tipoLancamento(TipoLancamento.D)
//	 			.build();
//		lancamento.addDatalheLancamento(detalheLancamento);
//		
//
////--------------- Transfere para Conta/Carteira -----------------------------------     
//	if ( dto.getContaTransferencia() != null) { 
//	 	DetalheLancamento detalheLancTransf  = new DetalheLancamento().builder() 
//	 			.prestacao(1)
//	 			.valor(lancamento.getValorPrestacao()) 
//	 			.tipoLancamento(TipoLancamento.C)
//	 			.dataVenc(data)
//	 			.contaLancamento(dto.contaTransferencia)
//	 			.contaTransferencia(dto.contaLancamento)
//	 			.ano(data.getYear()) 
//	 			.mes(data.getMonthValue())
//	 			.conciliado(dto.conciliado)
//	 			.build();
//		lancamento.addDatalheLancamento(detalheLancTransf);
//		}
//   return lancamento; 
//}
//   private Lancamento comprarCheque(LancamentoDTO dto) {
//		int nroPrestacao      = dto.nroPrestacao;
//		int prestacaoInicial  = dto.getNroInicialPrestacao();
//		LocalDate data = dto.getDataDoc();
//	  
//		Lancamento lancamento = dtoToLancamento(dto);
//		for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
//			data = data.plusMonths(count);
//			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
//		 			.prestacao(count)
//		 			.valor(lancamento.getValorPrestacao()) 
//		 			.tipoLancamento(dto.tipoLancamento)
//		 			.dataVenc(data)
//		 			.contaLancamento(dto.contaLancamento)
//		 			.ano(data.getYear()) 
//		 			.mes(data.getMonthValue())
//		 			.conciliado(dto.conciliado)
//		 			.build();
//			lancamento.addDatalheLancamento(detalheLancamento);		
//		}
//	  return lancamento; 	
//  }
//   private Lancamento comprarCartao(LancamentoDTO dto) {
//   
//	    dto.setTipoLancamento(TipoLancamento.D);
//	    
//		Lancamento lancamento = dtoToLancamento(dto);
//		
//		
//		Conta conta = dto.contaLancamento; 
//		LocalDate data = conta.getDataFatura(dto.getDataDoc()); 
//
//		for(int count=dto.nroInicialPrestacao ; count <= dto.nroPrestacao; count++){
//			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
//					.prestacao(count)
//					.valor(lancamento.getValorPrestacao()) 
//					.tipoLancamento(dto.tipoLancamento)
//					.dataVenc(data)
//					.contaLancamento(dto.contaLancamento)
//					.ano(data.getYear()) 
//					.mes(data.getMonthValue())
//					.conciliado(true)
//					.build();
//			lancamento.addDatalheLancamento(detalheLancamento);
//			data = data.plusMonths(1);
//		}
//		return lancamento; 	
//  }
	@Override
	public void updateEntity(LancamentoDTO dto, Lancamento entity) {
		TipoOperacao  tipoOperacao = dto.getTipoOperacao();
		Converter converter = converters.getConverter(tipoOperacao.toString());
	     converter.updateEntity(dto, entity);  
//		
//		      entity.setFavorecido(dto.favorecido) ;
//		      entity.setSubCategoria(dto.subCategoria); 
//		      entity.setInformacao(dto.getInformacao()); 
//		      entity.setObservacao(dto.getObservacao());
//		      
//		      
//		      entity.getDetalheLancamento().get(0).setConciliado(dto.conciliado); 
//		      entity.getDetalheLancamento().get(0).setDataVenc(dto.dataVenc);
		      
		      
	}

	

}
