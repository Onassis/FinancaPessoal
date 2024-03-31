package br.com.fenix.dominio.dto;

import br.com.fenix.dominio.enumerado.TipoLancamento;
import jakarta.persistence.*;

public class SubCategoriaDTO {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Long  id;
		private String descricao;
	    @Enumerated(EnumType.STRING)
		private TipoLancamento tipoLancamento;
	    private String tipoCategoria;
		public SubCategoriaDTO(Long id, String descricao, TipoLancamento tipoLancamento,String tipoCategoria) {
			super();
			this.id = id;
			this.descricao = descricao;
			this.tipoLancamento = tipoLancamento;
			this.tipoCategoria = tipoCategoria;
			
		}	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public TipoLancamento getTipoLancamento() {
			return tipoLancamento;
		}
		public void setTipoLancamento(TipoLancamento tipoLancamento) {
			this.tipoLancamento = tipoLancamento;
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
			SubCategoriaDTO other = (SubCategoriaDTO) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		public String getTipoCategoria() {
			return tipoCategoria;
		}
		public void setTipoCategoria(String tipoCategoria) {
			this.tipoCategoria = tipoCategoria;
		}
	    
}
