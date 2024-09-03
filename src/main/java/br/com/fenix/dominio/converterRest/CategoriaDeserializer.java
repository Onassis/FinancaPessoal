package br.com.fenix.dominio.converterRest;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;


public class CategoriaDeserializer extends StdDeserializer<Categoria> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
    CategoriaRepositorio categoriaRP;	
	
	public CategoriaDeserializer() { 
        this(null); 
    } 

    public CategoriaDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Categoria deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.textValue();
        System.out.println("Dezerialização");
//        System.out.println(jp.getText()  );
        System.out.println(texto_id);        
        if (texto_id.isEmpty() ) {
			return null;
		}
		Long id = Long.valueOf(texto_id);
        if (id == 0 ) {
			return null;
		}
    	System.out.println("deserializar criar  subcategoria");
		System.out.println(id);
		Categoria categoria = categoriaRP.findById(id).get();
		return categoria;    
	}

}
