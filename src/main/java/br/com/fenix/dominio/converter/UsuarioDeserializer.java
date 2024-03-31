package br.com.fenix.dominio.converter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.repositorio.UsuarioRepositorio;

public class UsuarioDeserializer extends StdDeserializer<Usuario> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UsuarioRepositorio usuarioRP;
	
	public UsuarioDeserializer() { 
        this(null); 
    }  

    public UsuarioDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Usuario deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
        System.out.println("Dezerialização Usuario");
        System.out.println(texto_id);        
        if (texto_id.isEmpty()) {
			return new Usuario();
		}
		Long id = Long.valueOf(texto_id);
		System.out.println(id);
		Optional<Usuario> usuarioOp = usuarioRP.findById(id);
		return usuarioOp.get();    
	}

}
