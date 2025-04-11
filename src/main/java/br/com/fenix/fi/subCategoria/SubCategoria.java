package br.com.fenix.fi.subCategoria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.rest.CategoriaDeserializer;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.masterCategoria.MasterCategoria;
import br.com.fenix.fi.modeloCategoria.ModeloSubCategoria;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper=true)
//@NoArgsConstructor
@Data
public class SubCategoria extends MasterCategoria {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 878110273093281276L;
	

	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = false)	
	@JsonDeserialize(using =  CategoriaDeserializer.class)
    public Categoria categoria;
    
    @Column(name="desp_fixa", nullable=true)
    private boolean desp_fixa; 
    
    @Column(name="imp_renda", nullable=true)
    private boolean imp_renda; 
	
	public SubCategoria() {
		super();
		this.classe = "SC";
	}
 
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
//		this.setTipoLancamento(categoria.getTipoLancamento());
		this.setTipoCategoria(categoria.getTipoCategoria());
	}
	
	 public SubCategoria ( Categoria categoria, ModeloSubCategoria modeloSubCategoria) {
		 super();
		 this.categoria = categoria;
		 this.setDescricao(modeloSubCategoria.getDescricao());
         this.setTipoLancamento(modeloSubCategoria.getTipoLancamento());
	 }
	@Override
	public String toString() {
		return "SubCategoria [categoria=" + categoria + ", desp_fixa=" + desp_fixa + ", imp_renda=" + imp_renda
				+ ", descricao=" + descricao + ", tipoLancamento=" + tipoLancamento + ", inativo=" + inativo
				+ ", isDebito=" + debito + "]";
	}



	 
	 
}
