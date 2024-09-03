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
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;

public class ContaDeserializer extends StdDeserializer<Conta> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private ContaRepositorio contaRP;
	
	public ContaDeserializer() { 
        this(null); 
    } 

    public ContaDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Conta deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);       
        String texto_id = node.asText();
        System.out.println("Dezerialização Conta");
        System.out.println(texto_id);        
        if (texto_id.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(texto_id);
		System.out.println(id);
		Optional<Conta> contaOp = contaRP.findById(id);
		return contaOp.get();    
	}

}
