package br.com.fenix.fi.upload;

import java.io.IOException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.automacao.AutomacaoServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.detalheLancamento.DetalheLancamentoRepositorio;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoFactory;
import br.com.fenix.fi.lancamento.LancamentoRepositorio;
import br.com.fenix.fi.lancamento.LancamentoServico;
import br.com.fenix.fi.saldo.SaldoConta;
import br.com.fenix.fi.saldo.SaldoContaRepositorio;
import br.com.fenix.fi.saldo.SaldoServico;
import br.com.fenix.util.Coletor;
import br.com.fenix.util.Tag;
import com.webcohesion.ofx4j.io.OFXReader;
import com.webcohesion.ofx4j.io.nanoxml.NanoXMLOFXReader;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.ResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction;
import com.webcohesion.ofx4j.generated.SignonResponse;
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponse;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.OFXSettings;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;
import com.webcohesion.ofx4j.domain.data.banking.*;
import com.webcohesion.ofx4j.domain.data.signon.*;


@Service
public class LancAuxServico {

	@Autowired
	LancAuxRepositorio lancAuxRP;
	
	@Autowired
	SaldoContaRepositorio saldoRP;
	
	@Autowired
	ContaServico contaSC;
	
	@Autowired
	SaldoServico saldoSC;
	
	@Autowired
	AutomacaoServico autoSC;
	
	@Autowired
	LancamentoServico  lancSC;
	
	@Autowired
	LancamentoRepositorio  lancRP;
	@Autowired
	DetalheLancamentoRepositorio  detLancRP;
	
//	@Autowired
//	private ModelMapper modelMapper;
	

	/**
	 * Exclui e recria a carga de lancamentos Aux do arquivo OFX  
	 * @param lancamentosAux
	 * @return List<LancAux>
	 */
	@Transactional
	public List<LancAux>  excluiSalvaTodos(List<LancAux> lancamentosAux) {
		System.out.println("excluir Salva Lancamento DTO");
		lancAuxRP.deleteAll();
		return lancAuxRP.saveAll(lancamentosAux);	 
	}
	/**
	 * Gera lançamentos apartir do arquivo do banco OFX 
	 * 
	 * @param contaId  
	 * @param ofxStream
	 * @return Lista de lançamentos 
	 * @throws IOException
	 * @throws OFXParseException
	 */
	public List<LancAux> geraLancamentoAux(UUID contaId, InputStream ofxStream) throws RegistroNaoExisteException,IOException, OFXParseException {
		
	    
	       Conta conta = contaSC.buscarPorId(contaId).orElseThrow(() -> new RegistroNaoExisteException("Conta não cadastrada"));
	       
	       List<LancAux> lst_lancAux = processaOFX4(conta, ofxStream);     
//---------------- Ajusta os Lançamentos -------------------------------------------------- 

		   autoSC.automatizaLactoHash(lst_lancAux);
			

		   lancSC.conciliar(lst_lancAux); 
 
		   return excluiSalvaTodos(lst_lancAux);   
		  
	}
	public List<LancAux> atualizaSaldo (List<LancAux> lancamentos, BigDecimal saldoFinal ) {
		LancAux lancAux;
		 BigDecimal saldo = saldoFinal; 
		 
	     ListIterator<LancAux> iterator = lancamentos.listIterator(lancamentos.size());

	        // O loop continua enquanto houver um elemento anterior.
	        while (iterator.hasPrevious()) {
	            lancAux = iterator.previous();
	            lancAux.setSaldo(saldo); 
	            saldo = lancAux.getSaldoAnterior();
	        }
//	        lancAux = lancamentos.get(0); 
//	        saldo =  lancAux.getSaldoAnterior();
//	        lancAux.setSaldo(saldo);
	        		
	    return lancamentos;     		
	}
	public  List<LancAux>  processaOFX4(Conta conta, InputStream ofxStream) throws IOException, OFXParseException {
		
		BigDecimal saldoFinal = BigDecimal.ZERO; 
		
		List<LancAux> lancamentos = new ArrayList<>();
		
		AggregateUnmarshaller a = new AggregateUnmarshaller(ResponseEnvelope.class);
		   
		   ResponseEnvelope re = (ResponseEnvelope) a.unmarshal(ofxStream);

		   //objeto contendo informações como instituição financeira, idioma, data da conta.
		   com.webcohesion.ofx4j.domain.data.signon.SignonResponse sr = re.getSignonResponse();

		   //como não existe esse get "BankStatementResponse bsr = re.getBankStatementResponse();"
		   //fiz esse codigo para capturar a lista de transações

		   MessageSetType type = MessageSetType.banking;
		   ResponseMessageSet message = re.getMessageSet(type);

		   if (message == null) {
	           return lancamentos;
		   }    
		   List<BankStatementResponseTransaction> bank = ((BankingResponseMessageSet) message).getStatementResponses();
		     for (BankStatementResponseTransaction b : bank) {
		           System.out.println("cc: " + b.getMessage().getAccount().getAccountNumber());
		           System.out.println("ag: " + b.getMessage().getAccount().getBranchId());
		           System.out.println("balanço final: " + b.getMessage().getLedgerBalance().getAmount());
                   saldoFinal = BigDecimal.valueOf( b.getMessage().getLedgerBalance().getAmount()); 		           
		           System.out.println("dataDoArquivo: " + b.getMessage().getLedgerBalance().getAsOfDate());
		           List<Transaction> transacoes = b.getMessage().getTransactionList().getTransactions();
		           System.out.println("TRANSAÇÕES\n");
		           for (Transaction trx : transacoes) {
//	                   // Na v1.6, getAmount() retorna um Double. Convertemos para BigDecimal.
		        	   BigDecimal valor = BigDecimal.valueOf(trx.getAmount()); 
		        	   LancAux lanxAux = LancAux.builder()
		        			   .contaLancamento(conta)
//		                       .dataVenc(trx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
		                       .dataLanc(trx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
		                       .dataDoc(trx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())	                       
		                        .total(valor) 
			                    .valor(valor)
			                    .saldo(BigDecimal.ZERO)
			                    .informacao(trx.getMemo())
			                    .chaveBanco(trx.getId())
			                    .refBanco(trx.getReferenceNumber())
			                    .chaveBanco(trx.getId())
			                    .tipoLancamento( valor.compareTo(BigDecimal.ZERO) > 0  ? TipoLancamento.C : TipoLancamento.D)
			                    .tipoOperacao( valor.compareTo(BigDecimal.ZERO) > 0  ? TipoOperacao.CR : TipoOperacao.DB)		                    
			                    .build();
	  	        	   lancamentos.add(lanxAux);	   
		               System.out.println("tipo: " + trx.getTransactionType().name());
		               System.out.println("id: " + trx.getId());
		               System.out.println("data: " + trx.getDatePosted());
		               System.out.println("valor: " + trx.getAmount());
		               System.out.println("descricao: " + trx.getMemo());
			           System.out.println("referenNumber: " + trx.getCheckNumber());
			           System.out.println("referenNumber: " + trx.getReferenceNumber());
			           
			           
		               
		               System.out.println("");
		           }
		       }
		  lancamentos = atualizaSaldo(lancamentos,saldoFinal);
		  return lancamentos;   
	}

//	@Transactional 
//	public  Iterable<LancAux>  processaOFX(Long conta, InputStream ofxStream) throws IOException, OFXParseException {
//
//		Coletor coletorLanc  = new Coletor();
//
////---------------- Ajusta os Lançamentos -------------------------------------------------- 
//		autoSC.automatizaLactoHash(coletorLanc);
//		
////		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
////			autoSC.automatizarHash(lancDTO);
////		}
////		
//		for (LancAux lancAux : coletorLanc.getLancamentosAux()  ) {
//			lancSC.conciliar(lancAux);
//		}
//
////---------------- Acerta Saldo do LançamentoDTO --------------------------------------------------// 
//	
//		BigDecimal saldoAnt = coletorLanc.UltimoLancamento().getSaldoAnterior();
//		for (int i = coletorLanc.getLancamentosAux().size() -2 ; i  >= 0 ; i--)  {			
//			LancAux   lancAux = coletorLanc.getLancamentosAux().get(i);
//			 lancAux.setSaldo(saldoAnt);
//			 saldoAnt = lancAux.getSaldoAnterior();  			 			
//		}
//		return coletorLanc;
//	}
	
	private LancAux ultimoLancamento(ArrayList<LancAux> lancamentosAux) {
	      if (lancamentosAux.size() == 0) {
	            return null;
	        } else {
	            return lancamentosAux.get(lancamentosAux.size() - 1);
	        }
	}
	private SaldoConta ultimoSaldo(ArrayList<SaldoConta> ListaSaldo) {
	      if (ListaSaldo.size() == 0) {
	            return null;
	        } else {
	            return ListaSaldo.get(ListaSaldo.size() - 1);
	        }
	}

	@Transactional()
	public List<Lancamento>  gerarLancamento(List<LancAux> dados) {   
		
//---------------- Atualiza Lançamento existentes -----------------------------------------------// 		
		
		 dados.stream() 
		    .filter( l -> l.isUpdateLanc())
		    .map( l -> 		
		      detLancRP.atualizaConciliadacao(l.getDetalheDestinoId(),
		    		  			l.getChaveBanco(),
		    		  			l.getDataLanc(),
		    		  			LocalDateTime.now(),
		    		  			l.getValor())
		 ); 
//---------------- Salva Lançamento --------------------------------------------------// 
		List<Lancamento> lancamentos =  dados.stream()
                .filter( l -> l.isNovoLanc()) 
				.map( lanc -> LancamentoFactory.criar(lanc)) 
				  .collect(Collectors.toList());
		if (dados.isEmpty()) { 
			return lancamentos; 
		}		
		
		lancamentos = lancRP.saveAll(lancamentos);
		
		saldoSC.atualizaSaldoLancAux(dados) ;
		
		return lancamentos; 
		 
	}				
	public Coletor  processaCartao(Conta conta,String mesCarga,BigDecimal saldoIni,List<String> conteudo) {

		Coletor coletorLanc  = new Coletor();
		 LocalDate dataCartao;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		try {
			String sDia =  String.format("%02d",conta.getDiaVencimento());	
			sDia = sDia.concat("/");
			dataCartao = LocalDate.parse(sDia.concat(mesCarga), formatter);				
		} catch (Exception e) {
			dataCartao = LocalDate.now();
			dataCartao = LocalDate.of(dataCartao.getYear(), dataCartao.getMonthValue(), conta.getDiaVencimento());
		}

//---------------- Cria Lançamentos --------------------------------------------------// 
		
		for (String textoLinha  : conteudo) {
		       System.out.println(textoLinha);
	    		 coletorLanc.AddLancamentoCSV( conta, dataCartao,textoLinha );
  
		}
		//---------------- Saldo Inicial  --------------------------------------------------//
		
		dataCartao = dataCartao.minusMonths(1);


		
		//---------------- Ajusta os Lançamentos --------------------------------------------------
		
		autoSC.automatizaLactoHash(coletorLanc.getLancamentosAux());
		
//		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
//					autoSC.automatizarHash(lancDTO);
// 		}
			
		return coletorLanc;
	}	
	

	public Coletor  processInputCSV(Conta conta,List<String> conteudo) {

		Coletor coletorLanc  = new Coletor();

//---------------- Cria Lançamentos --------------------------------------------------// 
		
		for (String textoLinha  : conteudo) {
	         System.out.println(textoLinha);
	         Tag tag = new Tag (textoLinha);	
	         System.out.println(tag);
	         coletorLanc.AddLLancamento(conta, tag);
		}
//---------------- Ajusta os Lançamentos --------------------------------------------------// 
		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			autoSC.automatizarHash(lancDTO);
		}
//		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
//			lancSC.conciliar(lancDTO);
//		}
//---------------- Acerta Saldo do LançamentoDTO --------------------------------------------------// 
		BigDecimal saldo = coletorLanc.UltimoLancamento().acertaSaldo();
		for (int i = coletorLanc.getLancamentosAux().size() -2 ; i  >= 0 ; i--)  {
			LancAux   lancAux = coletorLanc.getLancamentosAux().get(i);
			      lancAux.setSaldo(saldo); 
		          saldo = lancAux.acertaSaldo(); 				 			
		}
		return coletorLanc;
	}
	
	


	 	
}
//
	
//	    public List<Lancamento> parse(InputStream ofxStream) {
//	        try {
//	            // Na versão 1.6, usamos o OfxV1Reader para ler o stream.
//	            OfxV1Reader reader = new OfxV1Reader();
//	            OFX ofx = reader.parse(ofxStream);
//
//	            // O objeto OFX contém os "message sets". Estamos interessados no de extrato bancário.
//	            BankMessageSetResponseMessageSet bankMessageSet = (BankMessageSetResponseMessageSet) ofx.getMessageSet("BANK");
//
//	            // Verificações para garantir que os dados existem antes de acessá-los.
//	            if (bankMessageSet == null || bankMessageSet.getStatementResponses() == null || bankMessageSet.getStatementResponses().isEmpty()) {
//	                return Collections.emptyList();
//	            }
//
//	            // Um arquivo OFX pode ter múltiplas respostas de extrato, mas geralmente tem apenas uma.
//	            // Pegamos a primeira.
//	            StatementResponse statementResponse = bankMessageSet.getStatementResponses().get(0);
//	            TransactionList transactionList = statementResponse.getTransactionList();
//
//	            if (transactionList == null || transactionList.getTransactions() == null) {
//	                return Collections.emptyList();
//	            }
//
//	            // A partir da lista de transações, fazemos o mapeamento para nosso objeto Lancamento.
//	            List<Transaction> ofxTransactions = transactionList.getTransactions();
//	            List<Lancamento> lancamentos = new ArrayList<>();
//
//	            for (Transaction trx : ofxTransactions) {
//	                Lancamento lancamento = Lancamento.builder()
//	                    // Converte java.util.Date para java.time.LocalDate
//	                    .data(trx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
//	                    // Na v1.6, getAmount() retorna um Double. Convertemos para BigDecimal.
//	                    .valor(BigDecimal.valueOf(trx.getAmount()))
//	                    .descricao(trx.getMemo())
//	                    .idExterno(trx.getId())
//	                    .tipo(trx.getTransactionType() == TransactionType.DEBIT ? TipoLancamento.DEBITO : TipoLancamento.CREDITO)
//	                    .build();
//
//	                lancamentos.add(lancamento);
//	            }
//
//	            return lancamentos;
//
//	        } catch (OFXParseException | IOException e) {
//	            // É uma boa prática capturar exceções específicas da biblioteca.
//	            // Em uma aplicação real, use um logger (SLF4J) e lance uma exceção customizada.
//	            e.printStackTrace();
//	            throw new RuntimeException("Falha ao processar o arquivo OFX.", e);
//	        }
//	    }
//	}
//	
