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
public class CreditoConverter implements Converter<Credito,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(Credito entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public Credito ToEntity(LancamentoDTO dto) {
		Credito lancamento = new Credito(dto); 
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
	public void updateEntity(LancamentoDTO dto, Credito entity) {
	
		      entity.setFavorecido(dto.getFavorecido()) ;
		      entity.setSubCategoria(dto.getSubCategoria()); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      entity.setDataDoc(dto.getDataDoc());
		      entity.setTotal(dto.getTotal());
		      DetalheLancamento detLanc = entity.getDetalheLancamento().get(0);
		      
		      detLanc.setValor(dto.getTotal());
		      detLanc.setConciliado(dto.isConciliado()); 
		      detLanc.setDataVenc(dto.getDataVenc());
		      
		      
	}



	

}
