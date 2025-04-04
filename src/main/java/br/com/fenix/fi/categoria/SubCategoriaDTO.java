package br.com.fenix.fi.categoria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.rest.CategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SubCategoriaDTO extends CategoriaDTO {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@JsonDeserialize(using =  CategoriaDeserializer.class)
	    public Categoria categoria2;
	    
}
