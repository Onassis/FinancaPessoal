package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;

@Component
public class TransferenciaConverter implements Converter<Transferencia,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(Transferencia entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public Transferencia ToEntity(LancamentoDTO dto) {

		Transferencia lancamento = new Transferencia(dto);
  
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
	@Override
	public void updateEntity(LancamentoDTO dto, Transferencia entity) {
	
		      entity.setFavorecido(dto.favorecido) ;
		      entity.setSubCategoria(dto.subCategoria); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      
		      
		      entity.getDetalheLancamento().get(0).setConciliado(dto.conciliado); 
		      entity.getDetalheLancamento().get(0).setDataVenc(dto.dataVenc);
		      
		      
	}

	

}
