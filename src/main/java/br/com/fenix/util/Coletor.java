package br.com.fenix.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import lombok.Data;
import lombok.Getter;


public class Coletor {
	
	private BigDecimal saldoFinal = new BigDecimal(0); 
    private List<Tag> openTags = new LinkedList<>();
    private ArrayList<LancAux> lancamentosAux =  new ArrayList<>();
    
    

    public void end() {

    }


    public Coletor(Conta conta, List<String> conteudo) {
    	  super();
//    	  processInput( conta,conteudo); 	  
    }

	public Coletor() {
		// TODO Auto-generated constructor stub
	}

    public void OrdenaDescendente () {
		Collections.sort(lancamentosAux , Collections.reverseOrder());   
    }
    public void OrdenaAcendente () {
    	Collections.sort(lancamentosAux);   
    }
    
    public ArrayList<LancAux>  getLancamentosAux() {
    	return this.lancamentosAux;
    }
	public void AddLLancamento(Conta conta,Tag tag) {
		   BigDecimal saldoAnterior; 
		   
			openTags.add(tag); 
			
         	switch (tag.getTagNome()) {
	        	case "STMTTRN"    : {   
	        							lancamentosAux.add(new LancAux(conta));
	        						    UltimoLancamento().setSaldo(new BigDecimal(0));
	        							break;
	        						}
	   
	        	case "TRNTYPE"    : System.out.println(tag.getTagValor() );
	        						if (tag.getTagValor().equals("CREDIT")) { 
	        							UltimoLancamento().setTipoLancamento(TipoLancamento.C); 
	        							UltimoLancamento().setLancamentoTipoOperacao(TipoOperacao.DP);
	        						}	
	        						else {
	        							
	        							UltimoLancamento().setTipoLancamento(TipoLancamento.D);
	        							UltimoLancamento().setLancamentoTipoOperacao(TipoOperacao.DB);
	        						}
	        						
	        						break;
	    		case "DTPOSTED"  : UltimoLancamento().setDataVenc(tag.getTagValorDate());
	    						   UltimoLancamento().setLancamentoDataDoc(tag.getTagValorDate());
	    						   UltimoLancamento().setConciliado(true);
	    						   break;

				// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  	    						   
	    		case "REFNUM"    : UltimoLancamento().setRefBanco(tag.getTagValor());	    			 	
	    							break;
	    		case "TRNAMT"    : 	    			
	    			 			UltimoLancamento().setValor (tag.getTagValorBigDecimal());
	    			 			        					    			 			
	    			 			break;
	    		// 	 ID da transação do banco			
	    		case "FITID"     : UltimoLancamento().setChaveBanco(tag.getTagValor()); break;	    		
	    		case "MEMO" 	 : UltimoLancamento().setLancamentoInformacao(tag.getTagValor()); break;
	    		// Saldo final
	    		case "BALAMT"    : 
	    						this.saldoFinal = tag.getTagValorBigDecimal();
	    						System.out.println("Tag "  + tag);
	    						UltimoLancamento().setSaldo(tag.getTagValorBigDecimal()); break;
	    		
        	}
    }
    

//		for( LancamentoDTO  lanc : lancamentosDTO) {
//			System.out.println(lanc);
//		}
    

    public BigDecimal getSaldoFinal() {
		return saldoFinal;
	}


	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
	}


	public LancAux UltimoLancamento() {
        if (lancamentosAux.size() == 0) {
            return null;
        } else {
            return lancamentosAux.get(lancamentosAux.size() - 1);
        }
    }
    public LancAux PrimeiroLancamento() {
        if (lancamentosAux.size() == 0) {
            return null;
        } else {
            return lancamentosAux.get(0);
        }
    }

}
