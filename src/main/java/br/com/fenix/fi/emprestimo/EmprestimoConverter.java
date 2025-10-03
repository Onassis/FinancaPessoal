package br.com.fenix.fi.emprestimo;

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
public class EmprestimoConverter implements Converter<Emprestimo,EmprestimoDTO> {

	@Override
	public EmprestimoDTO ToDto(Emprestimo entity) {
		
		return new EmprestimoDTO(entity);
	}

	@Override
	public Emprestimo ToEntity(EmprestimoDTO dto) {
		DetalheLancamento detalheLancamento;
		Emprestimo lancamento = new Emprestimo(dto); 
		LocalDate data = dto.getDataVenc();
		
		if (!dto.isEntrada()) {
			data = data.plusMonths(1);
		}
		Conta conta = dto.getContaLancamento();
		if (conta != null) {  
			data = conta.getDataFatura(dto.getDataVenc()); 
		}
		/**
		 * Efetua o credito na conta referente ao emprestimo 
		 */
		detalheLancamento = new DetalheLancamento().builder() 
				.prestacao(1)
				.tipoLancamento(TipoLancamento.C)
				.conciliado(true)
				.contaLancamento(dto.getContaLancamento())
				.valor(dto.getTotal()) 
				.dataVenc(dto.getDataDoc())
				.dataPgto(dto.getDataDoc())				
				.ano(dto.getDataDoc().getYear()) 
				.mes(dto.getDataDoc().getMonthValue())
				.contaLancamento(dto.getContaLancamento())			
				.build();
		lancamento.addDatalheLancamento(detalheLancamento);
		
		for(int count=dto.getNroInicialPrestacao() ; count <= dto.getNroPrestacao(); count++){ 
			
			detalheLancamento = new DetalheLancamento().builder() 
					.tipoLancamento(TipoLancamento.D)
					.prestacao(count)
					.valor(dto.getValor()) 
					.dataVenc(data)
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
	public void updateEntity(EmprestimoDTO dto, Emprestimo entity) {
	
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
