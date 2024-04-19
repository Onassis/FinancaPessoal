package br.com.fenix.dominio.servico;

import java.io.DataInput;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.*;
import org.modelmapper.PropertyMap;
import org.modelmapper.internal.bytebuddy.build.Plugin.Engine.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.api.exceptionhandle.EntidadeNaoEncontratException;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.SaldoConta;
import br.com.fenix.dominio.repositorio.DetalheLancamentoRepositorio;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SaldoContaRepositorio;
import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;

@Service
public class LancamentoServico {
	@Autowired
	LancamentoRepositorio lancamentoRP;
	@Autowired
	DetalheLancamentoRepositorio DtlancamentoRP;
	@Autowired
	SaldoContaRepositorio saldoRP;
	@Autowired
	SaldoServico saldoSC;

   @Autowired
   private ModelMapper modelMapper;
		  
   public List<LancamentoDTO> findAll () {
		
		Iterable<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAll();
		
		List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
		
		for(DetalheLancamento detLac : detalheLancamentos) {      
			LancamentoDTO lancamentoDTO = modelMapper.map(detLac, LancamentoDTO.class);
			lancamentosDTO.add(lancamentoDTO);
		}	
		return lancamentosDTO;
		
	}	
	public LancamentoDTO findDetLanc (Long DetLancId) {
		
		Optional<DetalheLancamento>  optDelLac = DtlancamentoRP.findById(DetLancId); 
		
		if (optDelLac.isEmpty()) { 
			return new LancamentoDTO(); 
		}
		DetalheLancamento detLanc = optDelLac.get(); 
		Lancamento lanc = detLanc.getLancamento(); 
		LancamentoDTO lancamentoDTO = modelMapper.map(optDelLac.get(), LancamentoDTO.class); 
//Busca o registro de conta destino 			  
		if (lanc.isTransferencia()) { 
			  for(DetalheLancamento lancDest : lanc.getDatalheLancamento())  { 
				  if (!lancDest.equals(detLanc)) { 
					  lancamentoDTO.setContaDestino(lancDest.getContaLancamento()) ;						  
				  }					  
			  }
		  }			
		return lancamentoDTO;		
	}
	
	public List<LancamentoDTO> listaPorMesAno (String mesLancamento ) {
	    LocalDate dataInicio=null; 
	    
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		try {
			  dataInicio = LocalDate.parse("01".concat(mesLancamento), formatter);				
		} catch (Exception e) {
			 dataInicio = LocalDate.now();
			 dataInicio = LocalDate.of(dataInicio.getYear(), dataInicio.getMonthValue(), 1);
			// TODO: handle exception
		}
		  
 	  LocalDate DataFim =  dataInicio.with(TemporalAdjusters.lastDayOfMonth()); 
		
		  List<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAllBydataVenctoBetween(dataInicio,DataFim);
	
		  List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
		  
		  for(DetalheLancamento detLanc : detalheLancamentos) {      
			  LancamentoDTO lancamentoDTO = modelMapper.map(detLanc, LancamentoDTO.class); 
			  lancamentosDTO.add(lancamentoDTO);
		  }	
		
		  System.out.println("Lista todos ");
		  return lancamentosDTO;
	}
	
	@Transactional
    public Lancamento salvar (LancamentoDTO lancDTO) {
		Lancamento lancamento = criar (lancDTO);
/*		if ( lancDTO.getContaLancamento() != null) {
			 SaldoConta saldo = saldoSC.buscaSaldo(lancDTO.getContaLancamento(), lancDTO.getLancamentoDataDoc());
		     saldoRP.save(saldo);
		}    	
		if ( lancDTO.getContaDestino()  != null) { 
			SaldoConta saldoDest = saldoSC.buscaSaldo(lancDTO.getContaDestino(), lancDTO.getLancamentoDataDoc());
	     	saldoRP.save(saldoDest);
		}    
*/
		lancamento = lancamentoRP.save(lancamento); 
		return lancamento; 
	}

	public Lancamento criar (LancamentoDTO lancDTO  ) {

		Lancamento lancamento = null; 
		
		System.out.println("Criar ");
		System.out.println(lancDTO);
		
		if (lancDTO.getDetalheDestinoId() != null) { 
			return  alterar (lancDTO); 
			}
		
		if (lancDTO.getContaLancamento() == null) { 
			return  conta_a_vencer(lancDTO); 
		}	
		switch (lancDTO.getLancamentoTipoOperacao()){
		
// -------------------- Aplicacao -------------------------- *		

//			case AP , PO ->  lancamento = comprarCartao (lancDTO);

	
// -------------------- Cheque  -------------------------- 		
			case CH -> lancamento = comprarCheque (lancDTO);

// -------------------- Debito   -------------------------- 		

			case DB -> lancamento = sacar (lancDTO);
			case PG -> lancamento = sacar (lancDTO);
		
// -------------------- Credito -------------------------- 		
		
			case DP -> lancamento = depositar(lancDTO);
		//	case RD -> lancamento = pagar (lancDTO);   // Rendimento
		
// -------------------- Compra cartao de credito -------------------------- 				

			case CC -> lancamento = comprarCartao (lancDTO);		
		
// -------------------- Transferencia  -------------------------- 	
			case AP  -> lancamento = Transferir (lancDTO);
			case SQ 	 -> lancamento = Transferir (lancDTO);
			case PI 	 -> lancamento = Transferir (lancDTO);
			case TR 	 -> lancamento = Transferir (lancDTO);
		
// -------------------- Investimento e Resgate------------------------- 
		
//		case IV -> lancamento = pagar (lancDTO);  
//		case RG -> lancamento = pagar (lancDTO);

// -------------------- Emprestimo  ------------------------------------		
//			case EP -> lancamento = sacar (lancDTO);  // Emprestimo
		}
		
		return lancamento; 
	}
	
	
	private List<DetalheLancamento> DTO_to_DetalhaLancamento (LancamentoDTO lancDTO) {
		
		  List<DetalheLancamento> detLancs   = new ArrayList<DetalheLancamento>()  ;
		  BigDecimal valorPrestacao;
		
		    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
		    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
	
		    valorPrestacao = lancDTO.getValor().divide(new BigDecimal(nroPrestacao));
			
			for(int count=prestacaoInicial ; count <= nroPrestacao; count++){				
			 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
			 	
			 	LocalDate data = lancDTO.getLancamentoDataDoc().plusMonths(count-1);
			 	detalheLancamento.setDataVenc(data);
			 	if  (detalheLancamento.getContaLancamento() != null) { 
			 		if (detalheLancamento.getContaLancamento().contaIsCartao() ) { 
			 			data = LocalDate.of(data.getYear(), data.getMonthValue(), detalheLancamento.getContaLancamento().getDiaVencimento());
			 		}
			 	}	 
			 	detalheLancamento.setValor(valorPrestacao ); 
			 	detLancs.add(detalheLancamento);
	     }
		 		 
		 return detLancs;		 
	}
	@Transactional
	private Lancamento conta_a_vencer(LancamentoDTO lancDTO) {
	    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
	    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();

		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
		
		lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );
		
		lancamento.setDatalheLancamento( DTO_to_DetalhaLancamento(lancDTO));
		
		return lancamentoRP.save(lancamento); 	
	}	
	
	@Transactional
	private Lancamento comprarCheque(LancamentoDTO lancDTO) {
		 
	    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
	    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
	    BigDecimal valorPrestacao; 
	    Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
	    valorPrestacao = lancDTO.getValor().divide(new BigDecimal(nroPrestacao)); 
		for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
			
			 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);

			 	detalheLancamento.setPrestacao(count);
			 	LocalDate data = lancamento.getDataDoc().plusMonths(count);
			 	data = LocalDate.of(data.getYear(), data.getMonthValue(), detalheLancamento.getContaLancamento().getDiaVencimento());
			 	detalheLancamento.setDataVenc(data);
			 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
			 	detalheLancamento.setValor(valorPrestacao ); 
			 	lancamento.addDatalheLancamento(detalheLancamento);
	     }
		 return lancamento; 	
	}
  
	@Transactional
	public Lancamento sacar (LancamentoDTO lancDTO  ) {
				
		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);		
		lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );		
		DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
     	detalheLancamento.setTipoLancamento(TipoLancamento.D);
     	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	
		lancamento.addDatalheLancamento(detalheLancamento);
    	return lancamento; 
	}
	@Transactional
	public Lancamento depositar (LancamentoDTO lancDTO  ) {	
			Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
			lancamento.setTipoOperacao(TipoOperacao.DP);
			lancamento.setNroInicialPrestacao(1);

			DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
	     	detalheLancamento.setTipoLancamento(TipoLancamento.C);	     	
	     	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	

			lancamento.addDatalheLancamento(detalheLancamento);
//	    	saldoRP.save(saldo);			
			return lancamento; 	
	}		
	@Transactional
	public Lancamento Transferir (LancamentoDTO lancDTO  ) {
				
		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);		
		lancamento.setDataDoc(lancDTO.getLancamentoDataDoc() );		
		DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
     	detalheLancamento.setTipoLancamento(TipoLancamento.D);
     	detalheLancamento.setDataVenc(lancDTO.getLancamentoDataDoc());     	

		lancamento.addDatalheLancamento(detalheLancamento);
    	
//--------------- Transfere para Conta/Carteira -----------------------------------     
       	if ( lancDTO.getContaDestino() != null) { 
       		    lancamento.setTransferencia(true);
         		DetalheLancamento detLancDestino = modelMapper.map(lancDTO, DetalheLancamento.class);    
                detLancDestino.setContaLancamento(lancDTO.getContaDestino()) ;           		
         		detLancDestino.setDataVenc(lancDTO.getLancamentoDataDoc());     	
         		detLancDestino.setTipoLancamento(TipoLancamento.C);
          		lancamento.addDatalheLancamento(detLancDestino);     	     		
     		}
     return lancamento; 
	}

   @Transactional
   public Lancamento comprarCartao(LancamentoDTO lancDTO  ) {
 
    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
    BigDecimal valorPrestacao; 
    LocalDate data;
 
    Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);

/* ---------------------------------------------------------------------------------
*  Ajusta a data da compra passada para atual 	 
------------------------------------------------------------------------------------*/
  	 data = lancamento.getDataDoc();
  	 
	 data = lancDTO.getContaLancamento().getDataFatura(data);
	 
	 System.out.println("Data compra " + data); 
  	 
     if ( !data.isAfter(LocalDate.now()) ) { 
    	 data = LocalDate.of(LocalDate.now().getYear(), 
    			 			 LocalDate.now().getMonthValue(), 
    			 			 data.getDayOfMonth());

     }
// ------------------------------------------------------------------------------         
//    Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
	
//	SaldoConta saldo = saldoSC.buscaSaldo(lancDTO.getContaLancamento(), data);
    valorPrestacao = lancDTO.getValor().divide(new BigDecimal(nroPrestacao)); 
	lancamento.setTipoOperacao(TipoOperacao.CC);
	 

//-------------------------------------------------------------------------------	 
	 for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
		
		 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
	
		 	detalheLancamento.setPrestacao(count);
		    detalheLancamento.setDataVenc(data);  	
		 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
		 	detalheLancamento.setValor(valorPrestacao ); 
		 	lancamento.addDatalheLancamento(detalheLancamento);
		 	data = data.plusMonths(1);
     }
	 System.out.println("compra parcelada");
//     saldoRP.save(saldo);  
	 return lancamento; 	
}
  @Transactional 
  public Lancamento alterar(LancamentoDTO lancDTO) {
	
	SaldoConta saldo; 
	long id = lancDTO.getDetalheLancamentoId();
	Optional<DetalheLancamento> optDetLanc =  DtlancamentoRP.findById(id); 
	DetalheLancamento detLanc = optDetLanc.get();
	Lancamento lancamento = detLanc.getLancamento();
	
	lancamento.setSubCategoria(lancDTO.getLancamentoSubCategoria());
    lancamento.setFavorecido(lancDTO.getLancamentoFavorecido());
	lancamento.setInformacao( lancDTO.getLancamentoInformacao());
	lancamento.setObservacao( lancDTO.getLancamentoObservacao());
	
	detLanc.setValor( lancDTO.getValor()); 
	detLanc.setDataVenc(lancDTO.getLancamentoDataDoc());
	
 	
	
    if (!detLanc.possuiContaLancanto() ) {
    	
    	saldo = saldoSC.buscaSaldo(lancDTO.getContaLancamento() , lancDTO.getLancamentoDataDoc());            
    	detLanc.setContaLancamento(lancDTO.getContaLancamento());
    	saldoRP.save(saldo);
    }

    if (detLanc.possuiContaLancanto() ) { 
    	detLanc.setConciliado(lancDTO.isConciliado());
    }
    
		
//	saldo = saldoSC.buscaSaldo(lancDTO.getContaLancamento(), lancDTO.getDataLanc());
	
	

	return   lancamentoRP.save(lancamento);
      
}
    
public LancAux conciliar( LancAux  lancDTO) {
	// TODO Auto-generated method stub
	return lancDTO;
}
}

/*	
    List<DetalheLancamento> lancamentos ;
    lancamentos  = DtlancamentoRP.
    		findbyContaAndByDataLancamentoandByValor(lancDTO.getContaDestino(), 
    				lancDTO.getDataComp(), lancDTO.getValor());
    
	for (DetalheLancamento lancDet  : lancamentos  ) {
		if (lancDTO.getIdBanco().equals(lancDet.getIdBanco())) {
			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
			lancDTO.setDetalheLancamentoId(lancDet.getId());
			System.out.println(lancDTO.getConciliado() );
			return lancDTO;    			    			
		}
		if ( lancDTO.getLancamentoFavorecido().equals(lancDet.getLancamento().getFavorecido())) {    			
			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
			lancDTO.setDetalheLancamentoId(lancDet.getId());
			System.out.println(lancDTO.getConciliado() );
			return lancDTO;    			
		}
	}
	for (DetalheLancamento lancDet  : lancamentos  ) {
		if ( lancDTO.getLancamentoInformacao().contains(lancDet.getLancamento().getInformacao() )) {   			
			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
			lancDTO.setDetalheLancamentoId(lancDet.getId());
			return lancDTO;    			
		}
	}
	for (DetalheLancamento lancDet  : lancamentos  ) {
		lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
		lancDTO.setDetalheLancamentoId(lancDet.getId());	
		return lancDTO;
	}  
	return lancDTO;
}

	/**	@Transactional

	 * 	
	 
		public void gerarLancamento(ArrayList<LancAux> lancDTOs) {
			
			
	// --------------  Acerta Saldo Inicial -------------------------------------------//		
			LancAux lancDTOSaldo = lancDTOs.get(0); 
			
			System.out.println("gerar Lancamento");
			System.out.println(lancDTOSaldo);
			
			SaldoConta saldo = saldoSC.buscaSaldo(lancDTOSaldo.getContaLancamento(),lancDTOSaldo.getDataCompesacao()); 
			saldo.setSaldoInicial( lancDTOSaldo.getSaldo());
			saldo.setSaldo( lancDTOSaldo.getSaldo());
			saldoRP.save(saldo);
			
	// --------------  Cria Lancamento  -------------------------------------------//
			for (LancAux lancDTO  :  lancDTOs  ) {
				criar(lancDTO);
			}	
		}
		
		public LancAux conciliar( LancamentoDTO  lancDTO) {
			
	        List<DetalheLancamento> lancamentos ;
	        lancamentos  = DtlancamentoRP.findbyContaAndByDataLancamentoandByValor(lancDTO.getContaLancamento(), lancDTO.getDataLancamento(), lancDTO.getValor());
	    	for (DetalheLancamento lancDet  : lancamentos  ) {
	    		if (lancDTO.getIdBanco().equals(lancDet.getIdBanco())) {
	    			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
	    			lancDTO.setDetalheLancamentoId(lancDet.getId());
	    			System.out.println(lancDTO.getConciliado() );
	    			return lancDTO;    			    			
	    		}
	    		if ( lancDTO.getLancamentoFavorecido().equals(lancDet.getLancamento().getFavorecido())) {    			
	    			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
	    			lancDTO.setDetalheLancamentoId(lancDet.getId());
	    			System.out.println(lancDTO.getConciliado() );
	    			return lancDTO;    			
	    		}
	    	}
	    	for (DetalheLancamento lancDet  : lancamentos  ) {
	    		if ( lancDTO.getLancamentoInformacao().contains(lancDet.getLancamento().getInformacao() )) {   			
	    			lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
	    			lancDTO.setDetalheLancamentoId(lancDet.getId());
	    			return lancDTO;    			
	    		}
	    	}
	    	for (DetalheLancamento lancDet  : lancamentos  ) {
				lancDTO.setLancamentoId(lancDet.getLancamento().getId()); 
				lancDTO.setDetalheLancamentoId(lancDet.getId());	
				return lancDTO;
			}  
			return lancDTO;
		}

	*/	


