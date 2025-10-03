package br.com.fenix.fi.juros;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import junit.framework.TestCase;

public class JurosTest extends TestCase {
    @Test
	public void testJuros() {
		
		BigDecimal fator = Juros.Fator(new BigDecimal(10)); 
		System.out.println(fator);
		BigDecimal resposta = new BigDecimal(1.1);
		
		assertEquals(resposta,fator );
	}
    
//    public static void main(String[] args) {
//    	BigDecimal fator = Juros.Fator(new BigDecimal(10)); 
//		System.out.println(fator);
//		BigDecimal resposta = new BigDecimal(1.1);
//
//		if (resposta.compareTo(fator) == 1 ) {
//			System.out.println("ok");
//		}
//    }
    
	
}
