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



public class StringDeserializer extends StdDeserializer<String> {
	
	private static final long serialVersionUID = 1L;
	

	public StringDeserializer() { 
        this(null); 
    }
	
    protected StringDeserializer(Class<?> vc) {
		super(vc);
	}
    @Override   
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    	System.out.println("String");
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
      	
        if (texto_id.isEmpty()) {
        
			return new String("");
        }	
    	System.out.println(texto_id);
    	

        return texto_id;
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

