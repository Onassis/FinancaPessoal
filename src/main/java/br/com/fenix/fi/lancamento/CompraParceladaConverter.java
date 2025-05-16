package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;

@Component
public class CompraParceladaConverter implements Converter<CompraParcelada,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(CompraParcelada entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public CompraParcelada ToEntity(LancamentoDTO dto) {
		CompraParcelada lancamento = new CompraParcelada(dto); 
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
	public void updateEntity(LancamentoDTO dto, CompraParcelada entity) {
	
		      entity.setFavorecido(dto.favorecido) ;
		      entity.setSubCategoria(dto.subCategoria); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setObservacao(dto.getObservacao());
		      
		      
		      entity.getDetalheLancamento().get(0).setConciliado(dto.conciliado); 
		      entity.getDetalheLancamento().get(0).setDataVenc(dto.dataVenc);
		      
		      
	}



	

}
