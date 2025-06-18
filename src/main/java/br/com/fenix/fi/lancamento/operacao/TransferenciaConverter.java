package br.com.fenix.fi.lancamento.operacao;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;

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
	 			.tipoLancamento(dto.getTipoLancamento())
	 			.dataVenc(data)
	 			.contaLancamento(dto.getContaLancamento())
	 			.contaTransferencia(dto.getContaTransferencia())
	 			.ano(data.getYear()) 
	 			.mes(data.getMonthValue())
	 			.conciliado(dto.isConciliado())
	 			.tipoLancamento(TipoLancamento.D)
	 			.build();
		detalheLancamento.ajustaValor();
		lancamento.addDatalheLancamento(detalheLancamento);
		

//--------------- Transfere para Conta/Carteira -----------------------------------     
	if ( dto.getContaTransferencia() != null) { 
	 	DetalheLancamento detalheLancTransf  = new DetalheLancamento().builder() 
	 			.prestacao(1)
	 			.valor(lancamento.getValorPrestacao()) 
	 			.tipoLancamento(TipoLancamento.C)
	 			.dataVenc(data)
	 			.contaLancamento(dto.getContaTransferencia())
	 			.contaTransferencia(dto.getContaLancamento())
	 			.ano(data.getYear()) 
	 			.mes(data.getMonthValue())
	 			.conciliado(dto.isConciliado())
	 			.build();
	 	detalheLancTransf.ajustaValor();	 	
		lancamento.addDatalheLancamento(detalheLancTransf);
		}
   return lancamento; 
}
	@Override
	public void updateEntity(LancamentoDTO dto, Transferencia entity) {
	
		      entity.setFavorecido(dto.getFavorecido()) ;
		      entity.setSubCategoria(dto.getSubCategoria()); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      entity.setDataDoc(dto.getDataDoc());
		      entity.setTotal(dto.getTotal());
		      
		      DetalheLancamento detLanc = entity.getDetalheLancamento().get(0);
		      detLanc.setValor(dto.getValor());
		      detLanc.setConciliado(dto.isConciliado()); 
		      detLanc.setDataVenc(dto.getDataVenc());	      
		      
	}

	

}
