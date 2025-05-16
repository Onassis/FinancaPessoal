package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;

@Component
public class LancamentoConverter implements Converter<Lancamento,LancamentoDTO> {
	   @Autowired
	   ConverterLancamentoFactory converters;
	   
	@Override
	public LancamentoDTO ToDto(Lancamento entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public Lancamento ToEntity(LancamentoDTO dto) {
		
		
		Lancamento lancamento=null;
		
		switch (dto.getTipoOperacao()){
		
// -------------------- Aplicacao -------------------------- *		

//			case AP , PO ->  lancamento = comprarCartao (lancDTO);

	
// -------------------- Cheque  -------------------------- 		
//			case CH -> lancamento = comprarCheque (dto);

// -------------------- Debito   -------------------------- 		

//			case DB -> lancamento = sacar (dto);
//			case PG -> lancamento = sacar (dto);
		
// -------------------- Credito -------------------------- 		
		
//			case DP -> lancamento = depositar(dto);
		//	case RD -> lancamento = pagar (lancDTO);   // Rendimento
		
// -------------------- Compra cartao de credito -------------------------- 				

//			case CC -> lancamento = comprarCartao (dto);		
		
// -------------------- Transferencia  --------------------------
//			AP, SQ,PI,		
			case  TR 		 -> lancamento = Transferir (dto);
		
// -------------------- Investimento e Resgate------------------------- 
		
//		case IV -> lancamento = pagar (lancDTO);  
//		case RG -> lancamento = pagar (lancDTO);

// -------------------- Emprestimo  ------------------------------------		
//			case EP -> lancamento = sacar (lancDTO);  // Emprestimo
		}
     
		 return lancamento; 	
	}
	
	private Lancamento dtoToLancamento(LancamentoDTO dto  ) {
		
		return  new Lancamento().builder()
	    		.dataDoc(dto.dataDoc) 	    		
	    		.nroInicialPrestacao(dto.nroInicialPrestacao) 
	    		.nroPrestacao(dto.nroPrestacao)
	    		.informacao(dto.getInformacao()) 
	    		.observacao(dto.getObservacao())
//	    		.categoria(dto.getSubCategoria().getCategoria()) 
	    		.subCategoria(dto.subCategoria) 
	    		.favorecido(dto.favorecido)
	    		.tipoOperacao(dto.tipoOperacao)
	    		.total(dto.total)
	    		.build() ;
	    
	}

	public Lancamento Transferir (LancamentoDTO dto  ) {
	
		Lancamento lancamento = dtoToLancamento(dto);
  
		LocalDate data = lancamento.getDataDoc();

		DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
	 			.prestacao(1)
	 			.valor(lancamento.getValorPrestacao()) 
	 			.tipoLancamento(dto.tipoLancamento)
	 			.dataVenc(data)
	 			.contaLancamento(dto.contaLancamento)
	 			.contaTransferencia(dto.contaTransferencia)
	 			.ano(data.getYear()) 
	 			.mes(data.getMonthValue())
	 			.conciliado(dto.conciliado)
	 			.tipoLancamento(TipoLancamento.D)
	 			.build();
		lancamento.addDatalheLancamento(detalheLancamento);
		

//--------------- Transfere para Conta/Carteira -----------------------------------     
	if ( dto.getContaTransferencia() != null) { 
	 	DetalheLancamento detalheLancTransf  = new DetalheLancamento().builder() 
	 			.prestacao(1)
	 			.valor(lancamento.getValorPrestacao()) 
	 			.tipoLancamento(TipoLancamento.C)
	 			.dataVenc(data)
	 			.contaLancamento(dto.contaTransferencia)
	 			.contaTransferencia(dto.contaLancamento)
	 			.ano(data.getYear()) 
	 			.mes(data.getMonthValue())
	 			.conciliado(dto.conciliado)
	 			.build();
		lancamento.addDatalheLancamento(detalheLancTransf);
		}
   return lancamento; 
}
   private Lancamento comprarCheque(LancamentoDTO dto) {
		int nroPrestacao      = dto.nroPrestacao;
		int prestacaoInicial  = dto.getNroInicialPrestacao();
		LocalDate data = dto.getDataDoc();
	  
		Lancamento lancamento = dtoToLancamento(dto);
		for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
			data = data.plusMonths(count);
			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
		 			.prestacao(count)
		 			.valor(lancamento.getValorPrestacao()) 
		 			.tipoLancamento(dto.tipoLancamento)
		 			.dataVenc(data)
		 			.contaLancamento(dto.contaLancamento)
		 			.ano(data.getYear()) 
		 			.mes(data.getMonthValue())
		 			.conciliado(dto.conciliado)
		 			.build();
			lancamento.addDatalheLancamento(detalheLancamento);		
		}
	  return lancamento; 	
  }
   private Lancamento comprarCartao(LancamentoDTO dto) {
   
	    dto.setTipoLancamento(TipoLancamento.D);
	    
		Lancamento lancamento = dtoToLancamento(dto);
		
		
		Conta conta = dto.contaLancamento; 
		LocalDate data = conta.getDataFatura(dto.getDataDoc()); 

		for(int count=dto.nroInicialPrestacao ; count <= dto.nroPrestacao; count++){
			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
					.prestacao(count)
					.valor(lancamento.getValorPrestacao()) 
					.tipoLancamento(dto.tipoLancamento)
					.dataVenc(data)
					.contaLancamento(dto.contaLancamento)
					.ano(data.getYear()) 
					.mes(data.getMonthValue())
					.conciliado(true)
					.build();
			lancamento.addDatalheLancamento(detalheLancamento);
			data = data.plusMonths(1);
		}
		return lancamento; 	
  }
	@Override
	public void updateEntity(LancamentoDTO dto, Lancamento entity) {
	
		      entity.setFavorecido(dto.favorecido) ;
		      entity.setSubCategoria(dto.subCategoria); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      
		      
		      entity.getDetalheLancamento().get(0).setConciliado(dto.conciliado); 
		      entity.getDetalheLancamento().get(0).setDataVenc(dto.dataVenc);
		      
		      
	}

	

}
