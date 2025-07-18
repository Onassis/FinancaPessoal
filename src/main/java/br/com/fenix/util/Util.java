package br.com.fenix.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Util {
	  public static LocalDate getPrimeiroDiaDoMes(LocalDate data) {
	        return data.with(TemporalAdjusters.firstDayOfMonth());
	    }
}
