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
	 			.valor(dto.getValor()) 
	 			.tipoLancamento(dto.getTipoLancamento())
	 			.dataVenc(data)
	 			.contaLancamento(dto.getContaLancamento())
	 			.contaTransferencia(dto.getContaTransferencia())
	 			.ano(data.getYear()) 
	 			.mes(data.getMonthValue())
	 			.conciliado(dto.isConciliado())
	 			.dataPgto(dto.getDataPgto())
	 			.valorPgto(dto.getValorPgto())
	 			.tipoLancamento(TipoLancamento.D)
	 			.build();
		detalheLancamento.ajustarDataRef();
		lancamento.addDatalheLancamento(detalheLancamento);
		
		return lancamento; 	
  }
	@Override
	public void updateEntity(LancamentoDTO dto, Debito entity) {
	
		      entity.setFavorecido(dto.getFavorecido()) ;
		      entity.setSubCategoria(dto.getSubCategoria()); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setDataDoc(dto.getDataDoc());
		      entity.setTotal(dto.getTotal());
		      
		      
		      DetalheLancamento detLanc = entity.getDetalheLancamento().get(0);

		      detLanc.setValor(dto.getTotal());
		      detLanc.setConciliado(dto.isConciliado()); 
		      detLanc.setDataVenc(dto.getDataDoc());		
		      detLanc.setDataPgto(dto.getDataPgto());
		      detLanc.setValorPgto(dto.getValor());

		      
	}



	

}
