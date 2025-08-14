package br.com.fenix.fi.lancamento.operacao;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import java.util.Optional;


@Component
public class CartaoCreditoConverter implements Converter<CartaoCredito,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(CartaoCredito entity) {
		
		return new LancamentoDTO(entity);
	}

	@Override
	public CartaoCredito ToEntity(LancamentoDTO dto) {
		CartaoCredito lancamento = new CartaoCredito(dto); 
		LocalDate data = dto.getDataDoc();
		if (!dto.isEntrada()) {
			data = data.plusMonths(1);
		}
		Conta conta = dto.getContaLancamento();
		if (conta != null) {  
			data = conta.getDataFatura(dto.getDataDoc()); 
		}
		
		for(int count=dto.getNroInicialPrestacao() ; count <= dto.getNroPrestacao(); count++){
			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
					.prestacao(count)
					.valor(lancamento.getValorPrestacao()) 
					.tipoLancamento(dto.getTipoLancamento())
					.dataVenc(data)
					.dataPgto(data)
					.dataRef(data)
                    .conciliado(false)
					.contaLancamento(dto.getContaLancamento())
					.ano(data.getYear()) 
					.mes(data.getMonthValue())
					.build();
			lancamento.addDatalheLancamento(detalheLancamento);
			data = data.plusMonths(1);
		}
		return lancamento; 	
  }
	@Override
	public void updateEntity(LancamentoDTO dto, CartaoCredito entity) {
	
		      entity.setFavorecido(dto.getFavorecido()) ;
		      entity.setSubCategoria(dto.getSubCategoria()); 
		      entity.setInformacao(dto.getInformacao()); 
		      entity.setDataDoc(dto.getDataDoc());
//		      entity.setTotal(dto.getTotal());
		      DetalheLancamento detLanc = entity.getDetalheLancamento().get(0);

		      detLanc.setValor(dto.getValor());
		      detLanc.setConciliado(dto.isConciliado()); 
		      detLanc.setDataVenc(dto.getDataVenc());
		      
		      detLanc.setDataPgto(dto.getDataPgto()); 
		      detLanc.setValorPgto(dto.getValorPgto());

		      
	}



	

}
