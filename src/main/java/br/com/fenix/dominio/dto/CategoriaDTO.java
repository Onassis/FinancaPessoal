package br.com.fenix.dominio.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converterRest.StringDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CategoriaDTO implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
 
		private Long  id;

	    
		@JsonDeserialize(using = StringDeserializer.class)		
		private String descricao;
		
		@JsonDeserialize(using = StringDeserializer.class) 
	    @Enumerated(EnumType.STRING)
		private TipoLancamento tipoLancamento;
        private String tipoCategoria;
	    
		private Long idCategoria; 
		
		private boolean desp_fixa; 
		
	    private boolean imp_renda;


	//    @JsonDeserialize(using = StringDeserializer.class) 
	//    private String tipoCategoria;
    
		public CategoriaDTO(Long id, String descricao, TipoLancamento tipoLancamento,Long idCategoria, boolean desp_fixa, boolean imp_renda) {
			super();
			this.id = id;
			this.descricao = descricao;			
			this.tipoLancamento = tipoLancamento;
			if ( idCategoria == 0) { 
				this.tipoCategoria = "CT";						
			} else { 
				this.tipoCategoria = "SC";										
			}
			this.idCategoria = idCategoria;
			this.desp_fixa = desp_fixa; 
			this.imp_renda = imp_renda;
		     
//			this.tipoCategoria = tipoCategoria;			
		}
	
		 

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
