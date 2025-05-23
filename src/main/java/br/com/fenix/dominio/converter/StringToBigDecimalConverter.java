package br.com.fenix.dominio.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {
	
	public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
	    final NumberFormat format = NumberFormat.getNumberInstance(locale);
	    if (format instanceof DecimalFormat) {
	        ((DecimalFormat) format).setParseBigDecimal(true);
	    }
	    return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
	}
    @Override
    public BigDecimal convert(String source) {

    	System.out.println("StringToBigDecimalConverter");
        if (source == null || source.isEmpty()) {
            return new BigDecimal(0);
        }
        source = source.replaceAll("(?i)R\\$\\s*", "").trim();

        
        Locale localeBR = new Locale("pt", "BR");
        NumberFormat formatadorNumero = NumberFormat.getNumberInstance(localeBR);
        
        NumberFormat format = NumberFormat.getCurrencyInstance(localeBR);
        try {
            if (formatadorNumero instanceof DecimalFormat) {
                ((DecimalFormat) formatadorNumero).setParseBigDecimal(true);
                return (BigDecimal) formatadorNumero.parse(source);
            } else {
                // Fallback
                Number numero = formatadorNumero.parse(source);
                return new BigDecimal(numero.toString());
            }
          
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        	return  new BigDecimal(0);
     
		}
    }
    
}

