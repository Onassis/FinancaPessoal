package br.com.fenix.dominio.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    	if (p == null) {
    		return Boolean.FALSE;
    	}
        if (p.getText().isEmpty() || p.getText().isBlank()) {
        	return Boolean.FALSE;
        }
        if ("1".equals(p.getText()) || "true".equals(p.getText())) {
            return Boolean.TRUE;
        }
        if ("0".equals(p.getText()) || "false".equals(p.getText())) {
            return Boolean.FALSE;
        }
    
        return null;
    }

}
