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
		
        private String tipoCategoria;
	    
    	@JsonDeserialize(using =  CategoriaDeserializer.class)
	    public Categoria categoria;
    	
		private boolean desp_fixa; 
		
		private boolean credito; 
	    private boolean imp_renda;
		private long versao;


	//    @JsonDeserialize(using = StringDeserializer.class) 
	//    private String tipoCategoria;
    
//		protected void EntidadeToDTO(MasterCategoria categoria) { 
//			if (categoria instanceof SubCategoria) { 
//				SubCategoria subCategoria = (SubCategoria) categoria;
//				this.idCategoria = subCategoria.getCategoria().getId();				
//				this.tipoCategoria = "SC";										
//			} else {
//				this.tipoCategoria = "CT";						
//			}
//			
//			this.id = categoria.getId();
//			this.descricao = categoria.getDescricao();			
//			this.tipoLancamento = categoria.getTipoLancamento();
//
//			this.desp_fixa = false;; 
//			this.imp_renda = false;	    		    	
//	    }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CategoriaDTO other = (CategoriaDTO) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}	    
}
