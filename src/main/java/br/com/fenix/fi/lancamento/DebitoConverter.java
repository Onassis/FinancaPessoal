package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;

@Component
public class DebitoConverter implements Converter<Debito,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(Debito entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public Debito ToEntity(LancamentoDTO dto) {
		Debito lancamento = new Debito(dto); 
		Conta conta = dto.contaLancamento; 
		LocalDate data = conta.getDataFatura(dto.getDataDoc()); 

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
		
		return lancamento; 	
  }
	@Override
	public void updateEntity(LancamentoDTO dto, Debito entity) {
	
		      entity.setFavorecido(dto.favorecido) ;
		      entity.setSubCategoria(dto.subCategoria); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      
		      
		      entity.getDetalheLancamento().get(0).setConciliado(dto.conciliado); 
		      entity.getDetalheLancamento().get(0).setDataVenc(dto.dataVenc);
		      
		      
	}



	

}
