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

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.LancamentoDTO;
import br.com.fenix.dominio.modelo.SaldoConta;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
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

	
	ArrayList<Lancamento> lancamentos ; 
	ArrayList<SaldoConta> listSaldos = new ArrayList<>();
	
	
/*	
	  @Autowired
	   public LancamentoServico(ModelMapper modelMapper) {
	      this.modelMapper = modelMapper;
	      this.modelMapper.addMappings(skipModifiedFieldsMap);
	   }

	   PropertyMap<LancamentoDTO, Lancamento> skipModifiedFieldsMap = new PropertyMap<LancamentoDTO, Lancamento>() {
	      protected void configure() {
	         skip().getIt();
	         
	     }
	   };
	*/ 
	   
	public void init() {
		  TypeMap<LancamentoDTO, Lancamento> propertyMapper = modelMapper
				  .createTypeMap( LancamentoDTO.class,Lancamento.class)
				  .addMapping(LancamentoDTO::getDetalheLancamentoId, Lancamento::setId);
		
	}
	
	
	@Transactional
	public void gerarLancamento(ArrayList<LancamentoDTO> lancDTOs) {
		
	
      
// --------------  Acerta Saldo Inicial -------------------------------------------//		
		LancamentoDTO lancDTOSaldo = lancDTOs.get(0); 
		
		System.out.println("gerar Lancamento");
		System.out.println(lancDTOSaldo);
		
		SaldoConta saldo = saldoSC.buscaSaldo(lancDTOSaldo.getContaLancamento(),lancDTOSaldo.getDataCompesacao()); 
		saldo.setSaldoInicial( lancDTOSaldo.getSaldo());
		saldo.setSaldo( lancDTOSaldo.getSaldo());
		saldoRP.save(saldo);
		
// --------------  Cria Lancamento  -------------------------------------------//
		for (LancamentoDTO lancDTO  :  lancDTOs  ) {
			criar(lancDTO);
		}	
	}
	
	public LancamentoDTO conciliar( LancamentoDTO  lancDTO) {
		
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
	public List<LancamentoDTO> listaPorMesAno (String mesLancamento ) {
		    LocalDate dataInicio=null; 
		    
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			try {
				  dataInicio = LocalDate.parse(mesLancamento, formatter);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			  
			  LocalDate DataFim =  dataInicio.with(TemporalAdjusters.lastDayOfMonth()); 
			  
			  System.out.println(dataInicio);
			  System.out.println(DataFim);
		
			
			  List<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAllBydataLancamentoBetween(dataInicio,DataFim);
		
			  List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
			  
			  for(DetalheLancamento detLac : detalheLancamentos) {      
				  
				  LancamentoDTO lancamentoDTO = modelMapper
						  .map(detLac, LancamentoDTO.class)
						  ;
				  lancamentosDTO.add(lancamentoDTO);
			  }	
			
			  System.out.println("Lista todos ");
			  return lancamentosDTO;
		
//		return null;
	}
	public List<LancamentoDTO> findAll () {
	
		Iterable<DetalheLancamento> detalheLancamentos = DtlancamentoRP.findAll();
		
		List<LancamentoDTO> lancamentosDTO =  new ArrayList<LancamentoDTO>(); 
		
		for(DetalheLancamento detLac : detalheLancamentos) {      
			LancamentoDTO lancamentoDTO = modelMapper.map(detLac, LancamentoDTO.class);
			lancamentosDTO.add(lancamentoDTO);
		}	
			
		System.out.println("Lista todos ");
		return lancamentosDTO;
		
//		return null;
	}
	
	@Transactional
	public Lancamento criar (LancamentoDTO lancDTO  ) {

		Lancamento lancamento = null; 
		System.out.println("Criar ");
		System.out.println(lancDTO);
		
       lancDTO.setLancamentoTipoOperacao(TipoOperacao.DB);
		
		switch (lancDTO.getLancamentoTipoOperacao()){
		
/* -------------------- Aplicacao -------------------------- */		

//		case AP , PO ->  lancamento = comprarCartao (lancDTO);

/* -------------------- Cheque  -------------------------- */		
			case CH -> lancamento = pagar (lancDTO);
			case CP -> lancamento = comprarChequeParc (lancDTO);

/* -------------------- Debito   -------------------------- */		

			case DB -> lancamento = pagar (lancDTO);
			case PG -> lancamento = pagar (lancDTO);
		
/* -------------------- Credito -------------------------- */		
		
			case DP -> lancamento = comprarCartao (lancDTO);
			case RD -> lancamento = pagar (lancDTO);   // Rendimento
		
/* -------------------- Compra cartao de credito -------------------------- */				

			case CC -> lancamento = comprarCartao (lancDTO);
			case PC -> lancamento = comprarCartaoParcelado (lancDTO);
		
/* -------------------- Transferencia  -------------------------- */				
			case SQ -> lancamento = Transferir (lancDTO);
			case PI -> lancamento = Transferir (lancDTO);
			case TR -> lancamento = Transferir (lancDTO);
		
/* -------------------- Investimento e Resgate------------------------- */
		
//		case IV -> lancamento = pagar (lancDTO);  
//		case RG -> lancamento = pagar (lancDTO);

/* -------------------- Emprestimo  ------------------------------------ */		
			case EP -> lancamento = pagar (lancDTO);  // Emprestimo
		}
		
		return lancamento; 
	}
	
	private Lancamento comprarChequeParc(LancamentoDTO lancDTO) {
		 
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
			 	detalheLancamento.setDataLancamento(data);
			 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
			 	detalheLancamento.setValor(valorPrestacao ); 
			 	lancamento.addDatalheLancamento(detalheLancamento);
	     }
		 return lancamentoRP.save(lancamento); 	
	}


	@Transactional
	public Lancamento pagar (LancamentoDTO lancDTO  ) {

		System.out.println("Busca Ultimo Saldo ");

		System.out.println(lancDTO);
		
//		modelMapper.getConfiguration().setMethodAccessLevel(AccessLevel.PROTECTED);
		    		
		lancDTO.setDataCompesacao( lancDTO.getLancamentoDataDoc());
		
		
		List<SaldoConta> saldoContas = saldoSC.atualizaSaldo (lancDTO.getContaLancamento(), lancDTO.getDataCompesacao(), lancDTO.getValor());

		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
		DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
     	detalheLancamento.setDataCompensacao( lancDTO.getLancamentoDataDoc());
     	detalheLancamento.setDataLancamento(lancDTO.getLancamentoDataDoc());     	
     	detalheLancamento.setTipoLancamento(TipoLancamento.D);
     	detalheLancamento.setDataLancamento(lancDTO.getLancamentoDataDoc());     	
     	
    	
     	detalheLancamento.setDataLancamento(lancDTO.getLancamentoDataDoc());     	
     	detalheLancamento.setTipoLancamento(TipoLancamento.D);
		lancamento.addDatalheLancamento(detalheLancamento);

//--------------- Pagamento da fatura do Cartão -----------------------------------
    	if ( lancDTO.getContaDestino() != null) { 
    		DetalheLancamento detLancDestino = modelMapper.map(lancDTO, DetalheLancamento.class);
     	
    		detLancDestino.setTipoLancamento(TipoLancamento.C);
    		detLancDestino.setContaLancamento(lancDTO.getContaDestino()); 
     		lancamento.addDatalheLancamento(detLancDestino);
     		List<SaldoConta> saldoContasDest = saldoSC.atualizaSaldo (lancDTO.getContaDestino(), lancDTO.getDataCompesacao(), lancDTO.getValor());
        	saldoRP.saveAll(saldoContasDest);
     	}

    	saldoRP.saveAll(saldoContas);

		return lancamentoRP.save(lancamento); 
	}
		
	@Transactional
	public Lancamento Transferir (LancamentoDTO lancDTO  ) {
		
		System.out.println("lancamento DTO");
	  	System.out.println(lancDTO);
//	  	modelMapper.getConfiguration().setMethodAccessLevel(AccessLevel.PROTECTED);
	  	
		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
		System.out.println(lancamento);
     	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
 		detalheLancamento.setDataLancamento(lancDTO.getLancamentoDataDoc());     	     	
     	detalheLancamento.setDataLancamento(lancDTO.getLancamentoDataDoc());     	
     	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
     	lancamento.addDatalheLancamento(detalheLancamento);	     	
//--------------- Transfere para Conta/Carteira -----------------------------------     
     	if ( lancDTO.getContaDestino() != null) {
         	DetalheLancamento detLancDestino = modelMapper.map(lancDTO, DetalheLancamento.class);     		
     		detLancDestino.setDataLancamento(lancDTO.getLancamentoDataDoc());     	     		
     		detLancDestino.setTipoLancamento(TipoLancamento.D);
     		detLancDestino.setContaLancamento(lancDTO.getContaDestino()); 
     		lancamento.addDatalheLancamento(detLancDestino);
     	}
     	
		return lancamentoRP.save(lancamento); 
	}

    @Transactional
   public Lancamento comprarCartaoParcelado (LancamentoDTO lancDTO  ) {
 
    int nroPrestacao      = lancDTO.getLancamentoNroPrestacao();
    int prestacaoInicial  = lancDTO.getLancamentoNroInicialPrestacao();
    BigDecimal valorPrestacao; 
    
  //  System.out.println(lancDTO);
	
    Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
	
    
	 valorPrestacao = lancDTO.getValor().divide(new BigDecimal(nroPrestacao)); 
	 
//	 lancamento.setTipoLancamento(;
	 
	 lancamento.setTipoOperacao(TipoOperacao.PC);
	 
	 
	 for(int count=prestacaoInicial ; count <= nroPrestacao; count++){
		
		 	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
		    System.out.println(detalheLancamento);
		 	detalheLancamento.setPrestacao(count);
		 	
		 	
		 	LocalDate data = lancamento.getDataDoc().plusMonths(count);
		 	System.out.println("Data Parcela");
		 	 System.out.println(data);
		 	data = LocalDate.of(data.getYear(), data.getMonthValue(), detalheLancamento.getContaLancamento().getDiaVencimento());
		 	detalheLancamento.setDataLancamento(data);
		 	
		 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
	
		 	
		 	detalheLancamento.setValor(valorPrestacao ); 
		 	lancamento.addDatalheLancamento(detalheLancamento);
     }
	 System.out.println("compra parcelada");

	 return lancamentoRP.save(lancamento); 	
}
    @Transactional
   public Lancamento comprarCartao (LancamentoDTO lancDTO  ) {
 
    Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
    lancamento.setTipoOperacao(TipoOperacao.CC);
		 
  	DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
    System.out.println(detalheLancamento);
	detalheLancamento.setPrestacao(1);
 	detalheLancamento.setTipoLancamento(TipoLancamento.D);	
 	lancamento.addDatalheLancamento(detalheLancamento);
 	return lancamentoRP.save(lancamento); 	
}
    

	public Lancamento depositar (LancamentoDTO lancDTO  ) {
		Lancamento lancamento = modelMapper.map(lancDTO, Lancamento.class);
		lancamento.setTipoOperacao(TipoOperacao.CC);
		lancamento.setNroInicialPrestacao(1);
		DetalheLancamento detalheLancamento = modelMapper.map(lancDTO, DetalheLancamento.class);
		
	 	Conta conta     = detalheLancamento.getContaLancamento();
	   	conta.debitar(detalheLancamento.getValor());
		detalheLancamento.setPrestacao(1);
		detalheLancamento.setTipoLancamento(TipoLancamento.C);	
		lancamento.addDatalheLancamento(detalheLancamento);
		
		return lancamentoRP.save(lancamento); 	
	}



	
}
