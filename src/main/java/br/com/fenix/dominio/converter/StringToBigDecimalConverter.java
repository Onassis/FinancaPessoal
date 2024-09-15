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
        NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
        try {
        	  source = source.replace("R$", "").trim();
        	  System.out.println(source);
        	  BigDecimal number = parse(source, Locale.FRANCE);
            return number;  
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        	return  new BigDecimal(0);
     
		}
    }
}

