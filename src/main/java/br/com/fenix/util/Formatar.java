package br.com.fenix.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class Formatar {
	  private static  Locale LOCAL = new Locale("pt","BR");   
	 
	  private static  DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(LOCAL) ;

	  public static String FloatStr ( Float valor ) { 
		  	  
			return df.format(valor);   

	  }
}
