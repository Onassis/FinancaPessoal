package br.com.fenix.fi.detalheLancamento;

import org.springframework.stereotype.Component;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;


@Component
public class DetalheLancamentoConverter  implements Converter<DetalheLancamento,LancamentoDTO> {

	@Override
	public LancamentoDTO ToDto(DetalheLancamento entity) {
		return new LancamentoDTO(entity);
	}

	@Override
	public DetalheLancamento ToEntity(LancamentoDTO dto) {
		return null;
	}

	@Override
	public void updateEntity(LancamentoDTO dto, DetalheLancamento entity) {
		
		TipoOperacao tipo = dto.getTipoOperacao();
		if ( tipo == TipoOperacao.CP) { 
			entity.setValor(dto.getValor());
		}
		else { 
			entity.setValor(dto.getTotal());
		}
		
		entity.setConciliado(dto.isConciliado()); 
		entity.setDataVenc(dto.getDataVenc());		
		
	    Lancamento lanc = entity.getLancamento(); 
	    lanc.setFavorecido(dto.getFavorecido()) ;
	    lanc.setSubCategoria(dto.getSubCategoria()); 
	    lanc.setInformacao(dto.getInformacao()); 
	    lanc.setObservacao(dto.getObservacao());
	    lanc.setDataDoc(dto.getDataDoc());
	    lanc.setTotal(dto.getTotal());

	}
	

}
