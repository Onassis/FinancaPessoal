package br.com.fenix.fi.juros;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.fenix.fi.juros.FinancialMath.FinancialException;

public class Juros {
	
	/*
	 * VP 	=> P = Valor presente ou  capital principal.
	 * VF 	=> S = Valor futuro.
 	 * PMT 	=> R = Prestação ou recebimento.
 	 *  
	 * i = Taxa de juros por período de capitalização.
	 * n = Número de períodos

	 */

    // Custom exception
    public static class FinancialException extends Exception {
        public FinancialException(String message) {
            super(message);
        }
    }
    // Constants
    private static final BigDecimal PERCENTAGEM  = new BigDecimal(0.01f) ; 
    private static final BigDecimal CEM  = new BigDecimal(100f) ; 
    private static final int MAX_FIN = 100; // Assumed maximum size for MatFin array
    private static final int MAX_TEN = 1000; // Assumed maximum iterations
    private static final int CASA = 10; // Assumed error tolerance
    private static final double ERRO = 0.00000001; // Assumed error tolerance
    private static final String ERR_MAT_FIN = "Financial calculation error";
    
    public enum PgtoTipo {
        PO, AN
    }
 
	/*
	 *   Calcula potencia de BigDecimal 
	 */
	public static BigDecimal potencia  (BigDecimal exp , BigDecimal d) {
    	return BigDecimalCalculo.pow(exp , d);
    }
	public static BigDecimal potencia  (BigDecimal exp , long d) {
    	return BigDecimalCalculo.pow(exp , new BigDecimal(d));
    }
	// Retorn numero absoluto    
	public static BigDecimal log  (BigDecimal exp , BigDecimal d) {
    	return BigDecimalCalculo.log10(exp);
    }
	/**
     * Função do calula taxa de juros informada em percentual   
     */ 
	public static BigDecimal Taxa(BigDecimal i)  {  
		
		return i.divide(CEM);
				

	}
	/**
     * Função do calula fator do juros informada em percentual   
     */ 
	public static BigDecimal Fator (BigDecimal i) { 
		return  Taxa(i).add(BigDecimal.ONE) ; 	
	}
    /*
     * FATOR DE CAPITALIZAÇÃO
     * VF = VP * FPS(i,n) 
     * S = P * FPS(i,n)
     * FPS(i,n) = ( 1 + i )^n   
    */
	public static BigDecimal FPS(BigDecimal i, long n  ) throws FinancialException {	
		BigDecimal fps;
		  try {			  
			  fps =  potencia(Fator(i),new BigDecimal(n));
		 
		  } catch (ArithmeticException e) {
	            throw new FinancialException("Error FPS: " + e.getMessage());
	        }		  
		  return fps;
	}
	public static BigDecimal FPS(BigDecimal i, BigDecimal n  ) throws FinancialException {
		BigDecimal fps;
		  try {			  
			  fps =  potencia(Fator(i),n);
				 
		  } catch (ArithmeticException e) {
	            throw new FinancialException("Error FPS: " + e.getMessage());
	      }		  		 
		  return fps;

	}
	/*
     * TAXA DE CAPITALIZAÇÃO
     * VF = VP * FPS
     *  TPS(i,n) = ( 1 + i )^n  - 1
     */	
	public static BigDecimal TPS(BigDecimal i, long n  ) throws FinancialException {		
		return FPS(i,n).subtract(BigDecimal.ONE); 
	}
	public static BigDecimal TPS(BigDecimal i, BigDecimal n  ) throws FinancialException {		
		return FPS(i,n).subtract(BigDecimal.ONE); 
	}
	
	public static BigDecimal VF (BigDecimal VP, BigDecimal i, long n ) throws FinancialException {		
		return VP.multiply(FPS(i,n)); 		
	}
	
	public static BigDecimal VF (BigDecimal VP, BigDecimal i, BigDecimal n ) throws FinancialException {		
		return VP.multiply(FPS(i,n)); 		
	}

    /*
     * FATOR DE VALOR ATUAL
     * VP = VF * ( 1 + i )^-n 
     * P  = S  * FSP(i,n) 
     * FSP(i,n) = ( 1 + i )^-n
     */
	public static BigDecimal FSP(BigDecimal i, long n  ) throws FinancialException {
		
		return FPS(i,n * -1); 
		
	}
	public static BigDecimal FSP(BigDecimal i, BigDecimal n  )  throws FinancialException {
		
		return FPS(i,n.negate()); 
	}	
	public static BigDecimal VP (BigDecimal VF, BigDecimal i, long n ) throws FinancialException {		
		return VF.multiply(FSP(i,n)); 		
	}

    /*
     * retorna o Periodo 
     * log(	VF / VP ) / log ( 1 + i) 
     * 
     */
	
	public static BigDecimal periodo(BigDecimal VP, BigDecimal VF, BigDecimal i ) {
		BigDecimal aux = (VF.divide(VP)); 		
		return aux.divide(Fator(i)) ;		
	}
	/*
	 * SÉRIES PERIÓDICAS UNIFORMES
	 */
	
	/*
	 * FATOR DE ACUMULAÇÃO DE CAPITAL PERIODICO 
	 * VF = PMT * FRS
	 * FRS(i,n) = ( ( 1 + i )^n - 1) /  i 
	 *  
	 */
	public static BigDecimal FRS(BigDecimal i , long n ) throws FinancialException {
		return TPS(i,n).divide(i) ;
	}
	public static BigDecimal FRS(BigDecimal i , BigDecimal n ) throws FinancialException{
		return TPS(i,n).divide(i) ;
	}
	/*
	 * FATOR DE FORMAÇÃO DE CAPITAL
	 * PMT =  VF * FSR(i,n) 
	 * FSR(i,n) =  i / ( ( 1 + i )^n - 1) 
	 */
	public static BigDecimal FSR(BigDecimal i , long n ) throws FinancialException {
		return i.divide(TPS(i,n)) ;
	}
	public static BigDecimal FSR(BigDecimal i , BigDecimal n ) throws FinancialException {
		return  i.divide(TPS(i,n)) ;
	}
	/*
	 * FATOR DE RECUPERAÇÃO DE CAPITAL
	 */
	public static BigDecimal FPR(BigDecimal i , long n , PgtoTipo tipo ) throws FinancialException{
		BigDecimal fpr = i.multiply(FPS(i,n)).divide(TPS(i,n));
	  if (tipo == PgtoTipo.PO) { 
            
		return fpr;  
	 }
	  return fpr.multiply(FSR(i,1));  	  
	}
	public static BigDecimal FPR(BigDecimal i , BigDecimal n , PgtoTipo tipo )throws FinancialException{
		BigDecimal fpr = i.multiply(FPS(i,n)).divide(TPS(i,n));
		  if (tipo == PgtoTipo.PO) { 
	            
			return fpr;  
		 }
		  return fpr.multiply(FSR(i,1));  
	}

	

	

	
}
