package br.com.fenix.util;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import lombok.Data;
import lombok.Getter;

import br.com.fenix.util.CsvReader;
import br.com.fenix.util.Formatar;



public class GeraPrestacaoCond {
	
	 private static final String ADDRESS_FILE = "bradesco.csv";
	 
	 private ArrayList<LancamentoDTO> lancamentosDTO =  new ArrayList<>();
	 private MultipartFile file;
	 private Conta conta; 
	 
	 
	 private static char  delimitador =  ';'  ;
	
	 public GeraPrestacaoCond(Conta conta,MultipartFile file  ) throws IOException {
		 this.conta = conta; 
		 this.file = file; 
		 
	 }
	 
	 private static float converter( String texto ) { 
/*		 //final String COMMA_SEPERATED = "###.###,##";
		 final Locale LOCAL = new Locale("pt","BR");  
		 
		  DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(LOCAL) ;
*/		  
		  float valor;
		  texto = texto.replaceAll("\\.", ""); 
		  texto = texto.replaceAll("," , ".");
		 try { 
		    	valor = Float.parseFloat(texto);  
				
			} catch (NumberFormatException e) {
				valor  = 0.0f;
			} 
		 return valor;
	 }
	 /*			    			    
		 
	 private void lerArquivo()  {
		 float vlrcredito;
		 float vlrdebito;
		 float vlrsaldo;
		 Conta conta;
		 try {
				CsvReader lancamentoCSV  = new CsvReader(file.getInputStream(), delimitador );
			
				products.readHeaders();

				while (products.readRecord())
				{
					String Data = products.get("Data");
					String lanc  = products.get("Lan�amento");
					String Doc  = products.get("Dcto.");
					String credito  = products.get("Cr�dito (R$)");
					String debito  = products.get("D�bito (R$)");
					String saldo  = products.get("Saldo (R$)");
				    vlrcredito = converter(credito);
					vlrdebito  = converter(debito);
			    	vlrsaldo = converter(saldo);  
			    	
							
					if ( lanc.indexOf("ANTERIOR") > -1 )	{ 
						Setprestacao("3.1", vlrsaldo, 0) ;
						continue;
					}
					

				   if ( lanc.indexOf("PATRULHA") > -1 ) 	{
					   Setprestacao("2.9", vlrcredito,vlrdebito) ; 
					   continue;
				}
								
				   if ( lanc.indexOf("AGUA") > -1 )      { 
					   Setprestacao("2.2", vlrcredito,vlrdebito) ;
					continue;
					}
				   
								
				   if ( lanc.indexOf("INSS") > -1  ) 	
				      { Setprestacao("2.1", vlrcredito,vlrdebito) ;
					continue;
				   }
					
				   if ( lanc.indexOf("RESGATE") > -1 ||  lanc.indexOf("APLICACOES" ) > -1  )  {
						

					Setprestacao("3.4", vlrcredito,vlrdebito) ; 
					continue;
				   }

			      if ( lanc.indexOf("BRADESCO") >  -1  ) 	{ 
			    	  Setprestacao("2.8", vlrcredito,vlrdebito) ;
			    	  continue;
			      }
  				
			     if ( lanc.indexOf("TARIFA") >  -1   ||  lanc.indexOf("TAR " ) > -1 ) {
			    	 Setprestacao("2.10", vlrcredito,vlrdebito) ;
			    	 continue;
			     }
					
					if ( lanc.indexOf("VISA") > 0  )	{ 
						Setprestacao("2.11", vlrcredito,vlrdebito) ;
						continue;
					}
					
					if ( lanc.indexOf("LIQUIDACAO") >  -1  )	{ 
						Setprestacao("1.1", vlrcredito,vlrdebito) ;
						continue;
					}
	 			 
					if ( lanc.indexOf("DINAMICA") >  -1  ) { 
						Setprestacao("2.6", vlrcredito,vlrdebito) ; 	
						continue; 
					}
				
		 			 if ( lanc.indexOf("TELEFONE") >  -1  ) { 
		 				 Setprestacao("2.4", vlrcredito,vlrdebito) ;
		 				continue;
		 			 }
					 if ( lanc.indexOf("COPASA") >  -1  )  { 
						 Setprestacao("2.2", vlrcredito,vlrdebito) ;
						 continue;
					 }
					 if ( lanc.indexOf("CEMIG") >  -1  ) {
						Setprestacao("2.3", vlrcredito,vlrdebito) ;
						continue;
					 }									 
					 if ( lanc.indexOf("GAS") >  -1  ) {
							Setprestacao("2.5", vlrcredito,vlrdebito) ;
							continue;
					 }	
					 if ( lanc.indexOf("LIBRA") >  -1  ) {
							Setprestacao("2.1", vlrcredito,vlrdebito) ;
							continue;
					 }	
					 
					if ( lanc.indexOf("PAGTO") >  -1  ) {
  					     Setprestacao("2.5", vlrcredito,vlrdebito) ;
 					      continue;
					}
					System.out.println(lanc );
		     
				}		
				products.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		
		 Set set = prestacao.keySet(); // get set-view of keys 
		// get iterator 
		Iterator itr = set.iterator(); 
		while(itr.hasNext()) { 
		String str = (String) itr.next(); 
		System.out.println(  prestacao.get(str)); 
		}
	 
	 }
*/			  
/*	 
	private static void gerarPrestacao() { 
	     String outputFile = "prestacao.csv";
	     String key; 
	     ContaCorrente conta;
		
		// before we open the file check to see if it already exists
		   boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("Descricao");
				csvOutput.write("Cr�dito");
				csvOutput.write("D�bito");
				csvOutput.write("Saldo");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			// write out a few records
			key = "1.1";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( conta.getCredito() )); 			 
			} 	 
			csvOutput.endRecord();

			key = "2.1";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();

			key = "2.2";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();

			key = "2.3";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.4";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.5";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.6";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.7";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.8";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.9";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.10";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
			key = "2.11";
			csvOutput.write(key);
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 			 
			} 	 
			csvOutput.endRecord();
// -----------------------------Saldo ---------------------------------- 
			key = "3.1";
			csvOutput.write(key);
			csvOutput.write("");
			csvOutput.write("");			
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getSaldo() )); 			 
			} 	 
			csvOutput.endRecord();
// -----------------------------Receita ---------------------------------- 
			key = "1.1";
			csvOutput.write("3.2");
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( conta.getCredito() )); 			 
			} 	 
			csvOutput.endRecord();
// -----------------------------Despesa  ---------------------------------- 
			key = "3.3";
			csvOutput.write(key);
			csvOutput.endRecord();
// -----------------------------Aplicacao ---------------------------------- 
			key = "3.4";
			csvOutput.write(key);
					
			if ( prestacao.containsKey(key) )  {
				 conta = (ContaCorrente) prestacao.get(key) ;
				 csvOutput.write( Formatar.FloatStr( conta.getCredito() )); 	
				 csvOutput.write( Formatar.FloatStr( -1 * conta.getDebito() )); 	
			} 	 
			csvOutput.endRecord();

			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	
	
*/
}

