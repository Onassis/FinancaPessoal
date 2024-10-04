package br.com.fenix.dominio.converterRest;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.fenix.favorecido.Favorecido;
import br.com.fenix.favorecido.FavorecidoRepositorio;

public class FavorecidoDeserializer extends StdDeserializer<Favorecido> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private FavorecidoRepositorio favorecidoRP;
	
	public FavorecidoDeserializer() { 
        this(null); 
    } 

    public FavorecidoDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Favorecido deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
        System.out.println("Dezerialização");
        System.out.println(texto_id);        
        if (texto_id.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(texto_id);
		System.out.println(id);
		Optional<Favorecido> favorecidoOp = favorecidoRP.findById(id);
		return favorecidoOp.get();    
	}

}
