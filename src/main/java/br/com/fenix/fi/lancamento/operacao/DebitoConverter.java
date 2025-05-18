package br.com.fenix.fi.lancamento.operacao;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.lancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;

@Component
public class DebitoConverter implements Converter<Debito,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(Debito entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public Debito ToEntity(LancamentoDTO dto) {
		Debito lancamento = new Debito(dto); 
		Conta conta = dto.getContaLancamento(); 
		LocalDate data = conta.getDataFatura(dto.getDataDoc()); 

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
		lancamento.addDatalheLancamento(detalheLancamento);
		
		return lancamento; 	
  }
	@Override
	public void updateEntity(LancamentoDTO dto, Debito entity) {
	
		      entity.setFavorecido(dto.getFavorecido()) ;
		      entity.setSubCategoria(dto.getSubCategoria()); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      
		      
		      entity.getDetalheLancamento().get(0).setConciliado(dto.isConciliado()); 
		      entity.getDetalheLancamento().get(0).setDataVenc(dto.getDataVenc());
		      
		      
	}



	

}
