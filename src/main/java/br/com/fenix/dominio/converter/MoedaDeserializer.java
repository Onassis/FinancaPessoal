package br.com.fenix.dominio.converter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

public class MoedaDeserializer extends StdDeserializer<Moeda> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MoedaRepositorio moedaRP;
	
	public MoedaDeserializer() { 
        this(null); 
    } 

    public MoedaDeserializer(Class<?> vc) { 
        super(vc); 
    }
    @Override
    public Moeda deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
        if (texto_id.isEmpty()) {
			return null;
		}

		Optional<Moeda> moedaOp = moedaRP.findById(texto_id);
		return moedaOp.get();    
	}

}
