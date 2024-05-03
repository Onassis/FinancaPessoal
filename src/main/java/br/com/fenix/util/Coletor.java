package br.com.fenix.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import lombok.Data;
import lombok.Getter;
import java.util.Scanner;


public class Coletor {

	
	private BigDecimal saldoFinal = new BigDecimal(0); 
    private List<Tag> openTags = new LinkedList<>();
    private ArrayList<LancAux> lancamentos =  new ArrayList<>();
    
    private String pattern = "#.##0,0#";

    private DecimalFormatSymbols symbols;
    private DecimalFormat decimalFormat;


/*    public Coletor(Conta conta, List<String> conteudo) {
    	  super();
    	  symbols = new DecimalFormatSymbols();
    	  symbols.setGroupingSeparator(',');
    	  symbols.setDecimalSeparator('.');
    	  decimalFormat = new DecimalFormat(pattern, symbols);
    	  decimalFormat.setParseBigDecimal(true);	  
    }
*/
	public Coletor() {
		  super();
		  symbols = new DecimalFormatSymbols(Locale.ITALIAN);
    	  decimalFormat = new DecimalFormat("", symbols);
	}

    public void OrdenaDescendente () {
		Collections.sort(lancamentos , Collections.reverseOrder());   
    }
    public void OrdenaAcendente () {
    	Collections.sort(lancamentos);   
    }
    
    public ArrayList<LancAux>  getLancamentosAux() {
    	return this.lancamentos;
    }
	public void AddLancamentoCSV(Conta conta, LocalDate dataCartao, String linha ) {
		LancAux lanc = new LancAux(conta);
		String tag,sPrestacao;
		int prestacaoIni ;
    	int prestacaoFinal;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Scanner scanner = new Scanner(linha);
        scanner.useDelimiter(";");
        try {
          		tag = scanner.next();     
           		System.out.println(tag);          		
               	LocalDate date = LocalDate.parse(tag, formatter);
           
                lanc.setLancamentoDataDoc(date) ;
                
                lanc.setDataVenc(dataCartao) ; 
                lanc.setLancamentoTipoOperacao(TipoOperacao.CC); 
                
                prestacaoIni   = 1;
            	prestacaoFinal = 1;
            	
                tag = scanner.next();     
                lanc.setLancamentoInformacao(tag); 
                
                int tamanho =  tag.length();
                
                String prestacao = tag.substring(tamanho-5, tamanho); 
                if (prestacao.contains("/"))  {
                	try {
                		sPrestacao   =  tag.substring(tamanho-5, tamanho-3);
                		prestacaoIni = Integer.parseInt(sPrestacao);                 	
                		sPrestacao =  tag.substring(tamanho-2, tamanho);
                		prestacaoFinal = Integer.parseInt(sPrestacao);
                	} catch (Exception e){             		
                	}
                }                		               
                lanc.setLancamentoNroInicialPrestacao(prestacaoIni);
                lanc.setLancamentoNroPrestacao(prestacaoFinal);
                
          		tag = scanner.next();
          		tag = tag.replace("R$", "");
          		tag = tag.replaceAll(" ", ""); 	
           		System.out.println(tag);          		               	     
          	    BigDecimal valor = new BigDecimal( decimalFormat.parse(tag).toString());
          	    lanc.setLancamentoValor(valor.multiply( new BigDecimal(prestacaoFinal)));
          	    
          	    
          	    if (valor.compareTo(BigDecimal.ZERO) > 0) {
          	      lanc.setTipoLancamento(TipoLancamento.D); 
          	      }
          	      else {
              	      lanc.setTipoLancamento(TipoLancamento.C);          	    	  
          	      }

                lanc.setValor(valor); 
                scanner.close();
                lancamentos.add(lanc);              
            	    
           } catch (Exception e) {
        	   System.out.println(e);
           }        
	}
	public void AddLLancamento(Conta conta,Tag tag) {
		   BigDecimal saldoAnterior; 
		   
			openTags.add(tag); 
			
         	switch (tag.getTagNome()) {
	        	case "STMTTRN"    : {   
	        							lancamentos.add(new LancAux(conta));
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
        if (lancamentos.size() == 0) {
            return null;
        } else {
            return lancamentos.get(lancamentos.size() - 1);
        }
    }
    public LancAux PrimeiroLancamento() {
        if (lancamentos.size() == 0) {
            return null;
        } else {
            return lancamentos.get(0);
        }
    }

}
