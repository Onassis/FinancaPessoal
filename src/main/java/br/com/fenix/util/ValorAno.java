package br.com.fenix.util;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class ValorAno {

	private BigDecimal valor01 = BigDecimal.ZERO;
	private BigDecimal valor02 = BigDecimal.ZERO;
	private BigDecimal valor03 = BigDecimal.ZERO;
	private BigDecimal valor04 = BigDecimal.ZERO;
	private BigDecimal valor05 = BigDecimal.ZERO;
	private BigDecimal valor06 = BigDecimal.ZERO;
	private BigDecimal valor07 = BigDecimal.ZERO;
	private BigDecimal valor08 = BigDecimal.ZERO;
	private BigDecimal valor09 = BigDecimal.ZERO;
	private BigDecimal valor10 = BigDecimal.ZERO;
	private BigDecimal valor11 = BigDecimal.ZERO;
	private BigDecimal valor12 = BigDecimal.ZERO;

	@Transient
	private BigDecimal total; 

    public void zera() {
	   	 valor01 = BigDecimal.ZERO;
	   	 valor02 = BigDecimal.ZERO;
	   	 valor03 = BigDecimal.ZERO;
	   	 valor04 = BigDecimal.ZERO;
	   	 valor05 = BigDecimal.ZERO;
	   	 valor06 = BigDecimal.ZERO;
	   	 valor07 = BigDecimal.ZERO;
	   	 valor08 = BigDecimal.ZERO;
	   	 valor09 = BigDecimal.ZERO;
	   	 valor10 = BigDecimal.ZERO;
	   	 valor11 = BigDecimal.ZERO;
	   	 valor12 = BigDecimal.ZERO;
	   	 total   = BigDecimal.ZERO;
    }
	public BigDecimal getTotal() {
		total = valor01
				.add(valor02)
				.add(valor03)
				.add(valor04)
				.add(valor05)
				.add(valor06)
				.add(valor07)
				.add(valor08)
				.add(valor09)
				.add(valor10)
				.add(valor11)
				.add(valor12);

		return total; 
	}

	public BigDecimal valorMes( int mes) { 

		BigDecimal valorMes = BigDecimal.ZERO; 

		switch (mes) {
			case 1: valorMes =  valor01;
			break; 
			case 2: valorMes =  valor02;
			break; 
			case 3: valorMes =  valor03;
			break; 
			case 4: valorMes =  valor04;
			break; 
			case 5: valorMes =  valor05;
			break; 
			case 6: valorMes =  valor06;
			break; 
			case 7: valorMes =  valor07;
			break; 
			case 8: valorMes =  valor08;
			break; 
			case 9: valorMes =  valor09;
			break; 
			case 10 : valorMes =  valor10;
			break; 
			case 11: valorMes =  valor11;
			break; 
			case 12: valorMes =  valor12;
			break; 
		}
		return valorMes;
	}

	public void  valorMes( int mes , BigDecimal valorMes) { 

		switch (mes) {
			case 1: valor01 = valorMes ;
			break; 
			case 2: valor02 = valorMes   ;
			break; 
			case 3: valor03 = valorMes   ;
			break; 
			case 4: valor04 = valorMes   ;
			break; 
			case 5: valor05 = valorMes   ;
			break; 
			case 6: valor06 = valorMes   ;
			break; 
			case 7: valor07 = valorMes   ;
			break; 
			case 8: valor08 = valorMes   ;
			break; 
			case 9: valor09 = valorMes   ;
			break; 
			case 10 : valor10 = valorMes   ;
			break; 
			case 11: valor11 = valorMes   ;
			break; 
			case 12: valor12 = valorMes   ;
			break;
		}
	}

}
