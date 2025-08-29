package br.com.fenix.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Util {
	  public static LocalDate getPrimeiroDiaDoMes(LocalDate data) {
	        return data.with(TemporalAdjusters.firstDayOfMonth());
	    }
	  
	  public static BigDecimal iniciaValor(BigDecimal value) {
	        return value == null ? BigDecimal.ZERO : value;
	  }

	  public static BigDecimal absValor(BigDecimal value) {
	        return value == null ? BigDecimal.ZERO : value.abs();
	  }
	  
}
