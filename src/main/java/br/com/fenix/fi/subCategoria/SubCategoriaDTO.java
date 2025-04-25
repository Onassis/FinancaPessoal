package br.com.fenix.fi.subCategoria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.rest.CategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.categoria.CategoriaDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data

public class SubCategoriaDTO extends CategoriaDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
   	@JsonDeserialize(using =  CategoriaDeserializer.class)
    public CategoriaDTO categoria;
	
	public SubCategoriaDTO() {
		super();
		this.classe = "SC"; // SC - SubCategoria 
	}
	
	public SubCategoriaDTO(CategoriaDTO categoria) {
			super();
			this.categoria = categoria;
			this.classe = "SC"; // SC - SubCategoria
			this.setTipoLancamento(categoria.getTipoLancamento());
			this.setTipoCategoria(categoria.getTipoCategoria());
	}
	

	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
		this.classe = "SC"; // SC - SubCategoria
		this.setTipoLancamento(categoria.getTipoLancamento());
		this.setTipoCategoria(categoria.getTipoCategoria());
	}
	@Override
	public String ajuda() {
		return this.categoria.getDescricao() + "->" + this.descricao ;			
	}		
}
