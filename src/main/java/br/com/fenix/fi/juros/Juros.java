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
	 *   retorna o numero arrendodado 
	 */
	public static BigDecimal arred  (Double exp, int nroDecimal ) {
    	return new BigDecimal(exp).setScale(nroDecimal, RoundingMode.HALF_UP); 
    }
	public static BigDecimal arred  (BigDecimal exp, int nroDecimal ) {
    	return exp.setScale(nroDecimal, RoundingMode.HALF_UP); 
    }
	public static BigDecimal arred2  (BigDecimal exp ) {
    	return exp.setScale(2, RoundingMode.HALF_UP); 
    }
	public static BigDecimal arred2  (Double exp ) {
    	return new BigDecimal(exp).setScale(2, RoundingMode.HALF_UP); 
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
	public static BigDecimal log  (BigDecimal exp ) {
    	return BigDecimalCalculo.log10(exp);
    }
	/**
     * Função do calula taxa de juros informada em percentual   
     */ 
	public static BigDecimal Taxa(BigDecimal i)  {  
		
		return i.divide(CEM);
				

	}
	public static BigDecimal Taxa(Double i)  {
		var taxa = new BigDecimal(i);
		return taxa.divide(CEM) ; 
	}
	/**
     * Função do calula fator do juros informada em percentual   
     */ 
	public static BigDecimal Fator (BigDecimal i) { 
		return  Taxa(i).add(BigDecimal.ONE) ; 	
	}
	public static BigDecimal Fator (Double i) { 
		return  Taxa(i).add(BigDecimal.ONE) ; 	
	}
    /*
     * FATOR DE CAPITALIZAÇÃO
     * VF = VP * FPS(i,n) 
     * S = P * FPS(i,n)
     * FPS(i,n) = ( 1 + i )^n   
    */
	public static BigDecimal FPS(Double i, long n  ) throws FinancialException {	
		BigDecimal fps;
		  try {			  
			  fps =  potencia(Fator(i),new BigDecimal(n));
		 
		  } catch (ArithmeticException e) {
	            throw new FinancialException("Error FPS: " + e.getMessage());
	        }		  
		  return fps;
	}
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
	
	/*
	 * Retorna o valor futuro ou montante 
	 */
	
	public static BigDecimal VF (Double VP, Double i, long n ) throws FinancialException {	
		var P = new BigDecimal(VP) ; 		
		var S = P.multiply(FPS(i,n));		
		return S; 		
	}
	public static BigDecimal VF (BigDecimal VP, BigDecimal i, long n ) throws FinancialException {	
		var P = VP ; 		
		var S = P.multiply(FPS(i,n));		
		return S; 		
	}
	
	public static BigDecimal VF (BigDecimal VP, BigDecimal i, BigDecimal n ) throws FinancialException {		
		var P = VP ; 		
		var S = P.multiply(FPS(i,n));		
		return S; 		
	}

    /*
     * FATOR DE VALOR ATUAL
     * VP = VF * ( 1 + i )^-n 
     * P  = S  * FSP(i,n) 
     * FSP(i,n) = ( 1 + i )^-n
     */
	public static BigDecimal FSP(Double i, long n  ) throws FinancialException {
		
		return FPS(i,n * -1); 
		
	}
	public static BigDecimal FSP(BigDecimal i, long n  ) throws FinancialException {
		
		return FPS(i,n * -1); 
		
	}
	public static BigDecimal FSP(BigDecimal i, BigDecimal n  )  throws FinancialException {
		
		return FPS(i,n.negate()); 
	}	
	/*
	 * Retorna o valor presente 
	 */
	public static BigDecimal VP (Double VF, Double i, long n ) throws FinancialException {
		var S = new BigDecimal(VF) ; 		
		var P = S.multiply(FSP(i,n));		
		return P; 		
	}
	public static BigDecimal VP (BigDecimal VF, BigDecimal i, long n ) throws FinancialException {
		var S = VF ; 		
		var P = S.multiply(FSP(i,n));		
		return P; 		
	}
	public static BigDecimal VP (BigDecimal VF, BigDecimal i, BigDecimal n ) throws FinancialException {
		var S = VF ; 		
		var P = S.multiply(FSP(i,n));		
		return P; 		
	}

    /*
     * retorna o Periodo 
     * log(	VF / VP ) / log ( 1 + i) 
     * 
     */
	public static BigDecimal periodo(double VP, double VF, double i ) throws FinancialException {
		var S = new BigDecimal(VF);
		var P = new BigDecimal(VP);
		var taxa = new BigDecimal(i);
		try {
			return periodo(P,S,taxa);  
		
		} catch (ArithmeticException e) {
          throw new FinancialException("periodo: " + e.getMessage());
		}		  
	}
	public static BigDecimal periodo(BigDecimal VP, BigDecimal VF, BigDecimal i ) throws FinancialException {
		var S = VF;
		var P = VP; 		
		try {			  
			var aux = log(S.divide(P)); 
			
			var  logTaxa = log(Fator(i));
			System.out.println("S/P " + aux) ;
			System.out.println("logTaxa " + logTaxa) ;

			var  n = aux.divide(logTaxa,6, RoundingMode.HALF_UP) ;
			return n;	
		} catch (ArithmeticException e) {
          throw new FinancialException("periodo: " + e.getMessage());
		}			
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
