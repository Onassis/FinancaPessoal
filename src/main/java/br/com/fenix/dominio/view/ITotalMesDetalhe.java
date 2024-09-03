package br.com.fenix.dominio.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converterRest.ContaDeserializer;
import br.com.fenix.dominio.converterRest.MoneyDeserializer;
import br.com.fenix.fi.conta.Conta;


public interface ITotalMesDetalhe {
	
	  /**
	   * 
	 * 
	 */

	
		@JsonDeserialize(using = ContaDeserializer.class)
		 Conta getConta() ;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate getData();
	
	    @JsonDeserialize(using = MoneyDeserializer.class) 
		BigDecimal getValor() ;
}
