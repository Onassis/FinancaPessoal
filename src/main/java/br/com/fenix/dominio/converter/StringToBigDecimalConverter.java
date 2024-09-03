package br.com.fenix.dominio.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String source) {
    	System.out.println("StringToBigDecimalConverter");
        if (source == null || source.isEmpty()) {
            return null;
        }
        NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
        try {
        	  source = source.replace("R$", "").trim();
        	  System.out.println(source);
            Number number = format.parse(source);
            return BigDecimal.valueOf(number.doubleValue());     
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
            throw new IllegalArgumentException("Invalid format for BigDecimal: " + source, e);
		}
    }
}

