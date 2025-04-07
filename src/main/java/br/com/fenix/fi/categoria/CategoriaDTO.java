package br.com.fenix.fi.categoria;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.AbstrataDTO;
import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.dominio.converter.rest.CategoriaDeserializer;
import br.com.fenix.dominio.converter.rest.StringDeserializer;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CategoriaDTO  extends EntidadeAbstrata<Long> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	    
		@JsonDeserialize(using = StringDeserializer.class)		
		private String descricao;
		
		
		@JsonDeserialize(using = StringDeserializer.class) 
	    @Enumerated(EnumType.STRING)
		private TipoLancamento tipoLancamento;
		
        private TipoCategoria tipoCategoria;
	    
    	@JsonDeserialize(using =  CategoriaDeserializer.class)
	    public Categoria categoria;
    	
		private boolean desp_fixa; 
		
		private boolean credito; 
	    private boolean imp_renda;
	    private boolean inativo; 
	    private String classe;
	    
		private long versao;
		
		public String ajuda() {
			if (this.categoria == null) {
                return this.descricao;
            }
			return this.categoria.getDescricao() + "->" + this.descricao ;
			
		}

}
