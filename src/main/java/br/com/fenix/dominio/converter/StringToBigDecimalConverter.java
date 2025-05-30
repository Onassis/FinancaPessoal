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

        Locale brasil = new Locale("pt", "BR");

        // 1. Remove o símbolo da moeda (R$) e espaços em branco extras.
        //    Regex para remover "R$", "r$", com ou sem espaço depois.
        String valorNumerico = source.replaceAll("(?i)R\\$\\s*", "").trim();
        // valorNumerico agora deve ser algo como "1.234,57"

        // 2. Usa NumberFormat.getNumberInstance() para parsear a string numérica
        NumberFormat formatadorNumero = NumberFormat.getNumberInstance(brasil);

        try {
            if (formatadorNumero instanceof DecimalFormat) {
                ((DecimalFormat) formatadorNumero).setParseBigDecimal(true);
                BigDecimal valor = (BigDecimal) formatadorNumero.parse(valorNumerico); 
                return valor;
            } else {
                // Fallback
                Number numero = formatadorNumero.parse(valorNumerico);
                BigDecimal valor = new BigDecimal(numero.toString());
                return valor;
            }
        } catch (ParseException e) {
 //           System.err.println("Erro ao converter a string numérica '" + valorNumerico + "' (originada de '" + valorMoeda + "'): Formato inválido.");
            e.printStackTrace();
            throw new IllegalArgumentException("Formato de número inválido após remover símbolo da moeda: " + valorNumerico, e);
        }
    }
    
}

