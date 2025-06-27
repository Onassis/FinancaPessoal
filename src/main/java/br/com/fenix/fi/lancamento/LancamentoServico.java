package br.com.fenix.fi.lancamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.detalheLancamento.DetalheLancamentoRepositorio;
import br.com.fenix.fi.saldo.SaldoContaRepositorio;
import br.com.fenix.fi.saldo.SaldoServico;
import br.com.fenix.fi.upload.LancAux;
import jakarta.persistence.EntityManagerFactory;

@Service
public class LancamentoServico  extends ServicoAbstratoDTO<Lancamento,LancamentoDTO,Long> implements IServicoDTO<Lancamento,LancamentoDTO,Long> { 



	@Autowired
	LancamentoRepositorio lancamentoRP;
	@Autowired
	DetalheLancamentoRepositorio DtlancamentoRP;
//	@Autowired
//	SaldoContaRepositorio saldoRP;
	@Autowired
	SaldoServico saldoSC;
	@Autowired
	LancamentoConverter converter;

//	@Autowired
//	private ModelMapper modelMapper;


	public LancamentoServico() {
		super();
	}

	@Override
	public LancamentoRepositorio getRp() {
		// TODO Auto-generated method stub
		return lancamentoRP;
	}
	@Override
	public LancamentoConverter getConverter() {

		return converter; 

	}
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Lancamento antesDeSalvar  (Lancamento entidade) throws NegocioException { 
		if (entidade.isTransferencia()) { 
			DetalheLancamento detalhe = entidade.getDetalheLancamento().get(0);
			if (detalhe.getContaLancamento().equals(detalhe.getContaTransferencia()))  {
				 throw new NegocioException("Transferência não pode ser para a mesma conta");
			}
		}
		return entidade;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void depoisDeSalvar(Lancamento entidade) throws NegocioException {
		for (DetalheLancamento detalhe : entidade.getDetalheLancamento()) { 
			saldoSC.atualizaSaldo( detalhe.getContaLancamento(), detalhe.getDataVenc(),detalhe.getValor()) ;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirPorId(Long id)throws Exception {
		try {
			antesDeExcluir(id);

			DetalheLancamento detLanc = DtlancamentoRP.findById(id).orElseThrow(); 
			Long idLanc = detLanc.getLancamento().getId(); 
			if ( detLanc.getLancamento().tipoOperacao == TipoOperacao.CP) {
				DtlancamentoRP.deleteById(id);				
			}
			else {
				DtlancamentoRP.deleteById(id);								
				getRp().deleteById(idLanc);
			}

		} catch (Exception e) {
			handleException(OperacaoDB.DEL,e);
		}
	}

	/**
	 * Busca pelo ID do delalheLancamento 
	 */
	@Override
	@Transactional(readOnly = true)
	public LancamentoDTO buscaDTOPorId (Long id) throws RegistroNaoExisteException {
//		Lancamento  entidade = getRp().findDetalheLancamentoById(id).orElseThrow(() -> new RegistroNaoExisteException("Registro  não encontrato:" + id) );
//		LancamentoDTO dto =  getConverter().ToDto(entidade);
		DetalheLancamento detLanc = DtlancamentoRP.findById(id).orElseThrow();
		
		return new LancamentoDTO(detLanc); 
	}
	
	/**
	 * Lista os lancamentos de um mês de referência
	 * @param mesLancamento
	 * @return
	 */
	public List<LancamentoDTO> listaPorMesAno (String mesLancamento ) {
		LocalDate dataInicio=null; 

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		try {
			dataInicio = LocalDate.parse("01".concat(mesLancamento), formatter);				
		} catch (Exception e) {
			dataInicio = LocalDate.now();
			dataInicio = LocalDate.of(dataInicio.getYear(), dataInicio.getMonthValue(), 1);
		}

		LocalDate DataFim =  dataInicio.with(TemporalAdjusters.lastDayOfMonth()); 

		//  	    List<Lancamento> lancamentos = lancamentoRP.findAllBydataVenctoBetween(dataInicio, DataFim);
		List<DetalheLancamento> detLancamentos = DtlancamentoRP.findAllBydataVenctoBetween(dataInicio, DataFim); 

		List<LancamentoDTO> lancamentoDTO = 	    detLancamentos
				.stream()
				.map(dado -> new LancamentoDTO(dado))
				.collect(Collectors.toList());

		return lancamentoDTO; 

	}
	public LancAux conciliar( LancAux  lancDTO) {
		List<DetalheLancamento> lancamentos ;
		Optional<DetalheLancamento>  lancOpt;

		lancamentos  = DtlancamentoRP.
				findbyContaAndByDataVencandByValor (
						lancDTO.getContaDestino(), 
						lancDTO.getDataDoc(), lancDTO.getValor());

		for (DetalheLancamento lancDet  : lancamentos  ) {		
			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 			
			lancDTO.setDetalheDestinoId( lancDet.getId());
			lancDTO.setConciliado(true);
			return lancDTO;    			    			
		}
		lancamentos  = DtlancamentoRP.
				findbyDtVencBetweenAndByValor (
						lancDTO.getDataDoc().minusDays(30),
						lancDTO.getDataDoc().plusDays(30), lancDTO.getValor());


		lancOpt = lancamentos.stream()
				.filter(e -> e.getDataVenc().equals(lancDTO.getDataDoc()))
				.findFirst();

		if (lancOpt.isPresent()) {
			DetalheLancamento detLanc = lancOpt.get(); 
			lancDTO.setLancamentoId(detLanc.getLancamento().getId()); 			
			lancDTO.setDetalheDestinoId( detLanc.getId());
			lancDTO.setConciliado(true);
			return lancDTO; 
		}

		lancOpt = lancamentos.stream()
				.filter(e -> e.getValor().equals(lancDTO.getValor()))
				.findFirst();

		if (lancOpt.isPresent()) {
			DetalheLancamento detLanc = lancOpt.get(); 
			lancDTO.setLancamentoId(detLanc.getLancamento().getId()); 			
			lancDTO.setDetalheDestinoId( detLanc.getId());
			lancDTO.setConciliado(true);
			return lancDTO; 
		}


		return lancDTO;
	}
}

//public List<LancamentoDTO> findAll () {
//	
//	Iterable<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAll();
//	
//	List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
//	
//	for(DetalheLancamento detLac : detalheLancamentos) {      
//		LancamentoDTO lancamentoDTO = modelMapper.map(detLac, LancamentoDTO.class);
//		lancamentosDTO.add(lancamentoDTO);
//	}	
//	return lancamentosDTO;
//	
//}	
//public LancamentoDTO findDetLanc (Long DetLancId) {
//	
//	Optional<DetalheLancamento>  optDelLac = DtlancamentoRP.findById(DetLancId); 
//	
//	if (optDelLac.isEmpty()) { 
//		return new LancamentoDTO(); 
//	}
//	DetalheLancamento detLanc = optDelLac.get(); 
//	Lancamento lanc = detLanc.getLancamento(); 
//	 LancamentoDTO dto =  getConverter().ToDto(lanc); 
////	LancamentoDTO lancamentoDTO = modelMapper.map(optDelLac.get(), LancamentoDTO.class); 
////Busca o registro de conta destino 			  
////	if (lanc.isTransferencia()) { 
////		  for(DetalheLancamento lancDest : lanc.getDetalheLancamento())  { 
////			  if (!lancDest.equals(detLanc)) { 
////				  lancamentoDTO.setContaTransferencia (lancDest.getContaLancamento()) ;						  
////			  }					  
////		  }
////	  }			
////	return lancamentoDTO;		
//}
//
//
//@Transactional
//public Lancamento salvar (LancamentoDTO lancDTO) {
//	Lancamento lancamento = criar (lancDTO);
//	lancamento = lancamentoRP.save(lancamento); 		
//	LocalDate datasaldo = lancDTO.getLancamentoDataDoc();
//		saldoRP.f_atualiza_saldo(lancamento.getCriadoPor().getId(), 
//							 lancDTO.getContaLancamento().getId(),
//							 datasaldo,
//							 null);
//	return lancamento; 
//}
//
//public Lancamento criar (LancamentoDTO lancDTO  ) {
//
//	Lancamento lancamento = null; 
//	
//	System.out.println("Criar ");
//	System.out.println(lancDTO);
//	
//	if (lancDTO.getDetalheDestinoId() != null) { 
//		return  alterar (lancDTO); 
//		}
//	
//	if (lancDTO.getContaLancamento() == null) { 
//		return  conta_a_vencer(lancDTO); 
//	}	
//	switch (lancDTO.getLancamentoTipoOperacao()){
//	
////-------------------- Aplicacao -------------------------- *		
//
////		case AP , PO ->  lancamento = comprarCartao (lancDTO);
//
//
////-------------------- Cheque  -------------------------- 		
//		case CH -> lancamento = comprarCheque (lancDTO);
//
////-------------------- Debito   -------------------------- 		
//
//		case DB -> lancamento = sacar (lancDTO);
//		case PG -> lancamento = sacar (lancDTO);
//	
////-------------------- Credito -------------------------- 		
//	
//		case DP -> lancamento = depositar(lancDTO);
//	//	case RD -> lancamento = pagar (lancDTO);   // Rendimento
//	
////-------------------- Compra cartao de credito -------------------------- 				
//
//		case CC -> lancamento = comprarCartao (lancDTO);		
//	
////-------------------- Transferencia  -------------------------- 	
//		case AP  -> lancamento = Transferir (lancDTO);
//		case SQ 	 -> lancamento = Transferir (lancDTO);
//		case PI 	 -> lancamento = Transferir (lancDTO);
//		case TR 	 -> lancamento = Transferir (lancDTO);
//	
////-------------------- Investimento e Resgate------------------------- 
//	
////	case IV -> lancamento = pagar (lancDTO);  
////	case RG -> lancamento = pagar (lancDTO);
//
////-------------------- Emprestimo  ------------------------------------		
////		case EP -> lancamento = sacar (lancDTO);  // Emprestimo
//	}
//	
//	return lancamento; 
//}
//
//
//private List<DetalheLancamento> DTO_to_DetalhaLancamento (LancamentoDTO lancDTO) {
//	
//	  List<DetalheLancamento> detLancs   = new ArrayList<DetalheLancamento>()  ;
//	  BigDecimal valorPrestacao;
//	
//	    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
//	    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
//
//	    valorPrestacao = lancDTO.getLancamentoTotal().divide(new BigDecimal(nroPrestacao));
//		
//		for(int count=prestacaoInicial ; count <= nroPrestacao; count++){				
//		 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//		 	
//		 	LocalDate data = lancDTO.getLancamentoDataDoc().plusMonths(count-1);
//		 	detalheLancamento.setDataVenc(data);
//		 	if  (detalheLancamento.getContaLancamento() != null) { 
//		 		if (detalheLancamento.getContaLancamento().isContaCartao() ) { 
//		 			data = LocalDate.of(data.getYear(), data.getMonthValue(), detalheLancamento.getContaLancamento().getDiaVencimento());
//		 		}
//		 	}	 
//		 	detalheLancamento.setValor(valorPrestacao ); 
//		 	detLancs.add(detalheLancamento);
//    }
//	 		 
//	 return detLancs;		 
//}
//
//private Lancamento conta_a_vencer(LancamentoDTO lancDTO) {
//   int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
//   int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
//
//	Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
//	
//	lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );
//	
//	lancamento.setDatalheLancamento( DTO_to_DetalhaLancamento(lancDTO));
//	
//	return lancamento; 	
//}	
//
//
//private Lancamento comprarCheque(LancamentoDTO lancDTO) {
//	 
//   int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
//   int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
//   BigDecimal valorPrestacao; 
//   Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
//   valorPrestacao = lancDTO.getLancamentoTotal().divide(new BigDecimal(nroPrestacao)); 
//	for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
//		
//		 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//
//		 	detalheLancamento.setPrestacao(count);
//		 	LocalDate data = lancamento.getDataDoc().plusMonths(count);
//		 	data = LocalDate.of(data.getYear(), data.getMonthValue(), detalheLancamento.getContaLancamento().getDiaVencimento());
//		 	detalheLancamento.setDataVenc(data);
//		 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
//		 	detalheLancamento.setValor(valorPrestacao ); 
//		 	lancamento.addDatalheLancamento(detalheLancamento);
//    }
//	 return lancamento; 	
//}
//
//
//public Lancamento sacar (LancamentoDTO lancDTO  ) {
//			
//	Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);		
//	lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );		
//	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//	detalheLancamento.setTipoLancamento(TipoLancamento.D);
//	detalheLancamento.setValor(lancDTO.getLancamentoTotal());
//	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	
//	lancamento.addDatalheLancamento(detalheLancamento);
//	return lancamento; 
//}
//
//public Lancamento depositar (LancamentoDTO lancDTO  ) {	
//		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
//		lancamento.setTipoOperacao(TipoOperacao.DP);
//		lancamento.setNroInicialPrestacao(1);
//
//		DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//    	detalheLancamento.setTipoLancamento(TipoLancamento.C);	     	
//    	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	
//
//		lancamento.addDatalheLancamento(detalheLancamento);			
//		return lancamento; 	
//}		
//
//public Lancamento Transferir (LancamentoDTO lancDTO  ) {
//			
//	Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);		
//	lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );		
//	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//	detalheLancamento.setTipoLancamento(TipoLancamento.D);
//	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	
//
//	lancamento.addDatalheLancamento(detalheLancamento);
//	
////--------------- Transfere para Conta/Carteira -----------------------------------     
//  	if ( lancDTO.getContaDestino() != null) { 
//  		    lancamento.setTransferencia(true);
//    		DetalheLancamento detLancDestino = modelMapper.map(lancDTO, DetalheLancamento.class);    
//           detLancDestino.setContaLancamento(lancDTO.getContaDestino()) ;           		
//    		detLancDestino.setDataVenc(lancDTO.getLancamentoDataDoc());     	
//    		detLancDestino.setTipoLancamento(TipoLancamento.C);
//     		lancamento.addDatalheLancamento(detLancDestino);     	     		
//		}
//return lancamento; 
//}
//
//public Lancamento comprarCartao(LancamentoDTO lancDTO  ) {
//
//int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
//int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
//BigDecimal valorPrestacao; 
//LocalDate data;
//
//Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
//
///* ---------------------------------------------------------------------------------
//*  Ajusta a data da compra passada para atual 	 
//------------------------------------------------------------------------------------*/
//if (lancDTO.getDataVenc() != null) {
//	data = lancDTO.getDataVenc(); 
//} else {
//	 data = lancamento.getDataDoc();
//	 
////Verificar ??????????????????????????
//	 
////	 data = lancDTO.getContaLancamento().getDataSaldo(data);    	
//}
////------------------------------------------------------------------------------         
//valorPrestacao = lancDTO.getLancamentoTotal().divide(new BigDecimal(nroPrestacao)); 
//lancamento.setTipoOperacao(TipoOperacao.CC);
//
////-------------------------------------------------------------------------------	 
//for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
//	
//	 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
//
//	 	detalheLancamento.setPrestacao(count);
//	    detalheLancamento.setDataVenc(data);  	
//	 	detalheLancamento.setValor(valorPrestacao ); 
//	 	lancamento.addDatalheLancamento(detalheLancamento);
//	 	data = data.plusMonths(1);
//}
//System.out.println("compra parcelada");
//return lancamento; 	
//}
//
//public Lancamento alterar(LancamentoDTO lancDTO) {
//
//long id = lancDTO.getDetalheLancamentoId();
//Optional<DetalheLancamento> optDetLanc =  DtlancamentoRP.findById(id); 
//DetalheLancamento detLanc = optDetLanc.get();
//Lancamento lancamento = detLanc.getLancamento();
//
//lancamento.setSubCategoria(lancDTO.getLancamentoSubCategoria());
//lancamento.setFavorecido(lancDTO.getLancamentoFavorecido());
//lancamento.setInformacao( lancDTO.getLancamentoInformacao());
//lancamento.setObservacao( lancDTO.getLancamentoObservacao());
//detLanc.setValor( lancDTO.getLancamentoTotal());  
//detLanc.setDataVenc(lancDTO.getLancamentoDataDoc());	 	
//
//if (!detLanc.possuiContaLancamento() ) {    	
//	detLanc.setContaLancamento(lancDTO.getContaLancamento());
//}
//
//if (detLanc.possuiContaLancamento() ) { 
//	detLanc.setConciliado(lancDTO.isConciliado());
//}
//return   lancamento;      
//}
//
