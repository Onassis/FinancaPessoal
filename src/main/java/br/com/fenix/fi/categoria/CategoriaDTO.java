package br.com.fenix.fi.categoria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.dominio.converter.rest.StringDeserializer;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class CategoriaDTO  extends EntidadeAbstrata<Long> {
	
		/**
		 * 
		 */
	
		
	    
		@JsonDeserialize(using = StringDeserializer.class)
		@NotBlank
		@NotNull
		@Size(min = 2, max = 40)
		protected String descricao;
		
		
		@JsonDeserialize(using = StringDeserializer.class) 
	    @Enumerated(EnumType.STRING)
		protected TipoLancamento tipoLancamento;
		
		@JsonDeserialize(using = StringDeserializer.class) 
	    @Enumerated(EnumType.STRING)
		protected TipoCategoria tipoCategoria;
	    
   	
		protected boolean desp_fixa; 
		
		protected boolean credito; 
		protected boolean imp_renda;
		protected boolean inativo; 

		protected String classe; 
	    
		protected long versao;
		
		public CategoriaDTO() {
			super();
			this.classe = "CT"; // CT - Categoria
		}
		public void setTipoCategoria(TipoCategoria tipoCategoria) {
			this.tipoCategoria = tipoCategoria;
			 
		  if ( tipoCategoria == TipoCategoria.DP) {
			  this.tipoLancamento = TipoLancamento.D ; 
		  }
		  if ( tipoCategoria == TipoCategoria.RE) {
			  this.tipoLancamento = TipoLancamento.C ; 
		  }
		}
		public String ajuda() {
		    return this.descricao;		
		}


}
