package br.com.fenix.dominio.converter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;



public class MoneyDeserializer extends StdDeserializer<BigDecimal> {
	
	private static final long serialVersionUID = 1L;
	
	public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
	    final NumberFormat format = NumberFormat.getNumberInstance(locale);
	    if (format instanceof DecimalFormat) {
	        ((DecimalFormat) format).setParseBigDecimal(true);
	    }
	    return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
	}
	public MoneyDeserializer() { 
        this(null); 
    }
	
    protected MoneyDeserializer(Class<?> vc) {
		super(vc);
	}
    @Override   
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    	System.out.println("BigDecimal");
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
      	
        if (texto_id.isEmpty()) {
        	System.out.println("Nulo");
			return new BigDecimal(0);
        }	
    	System.out.println(texto_id);
    	
        BigDecimal bd = null;
		try {
			bd = parse(texto_id, Locale.FRANCE);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  new BigDecimal(0);
		};

        return bd;
    }
    
}

/*public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {

private NumberDeserializers.BigDecimalDeserializer delegate = NumberDeserializers.BigDecimalDeserializer.instance;

@Override
public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	System.out.println("BigDecimal");
	System.out.println(jp.toString());

	BigDecimal bd = delegate.deserialize(jp, ctxt);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd;
}    
*/

