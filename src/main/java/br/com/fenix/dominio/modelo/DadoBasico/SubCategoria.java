package br.com.fenix.dominio.modelo.DadoBasico;

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

import br.com.fenix.dominio.DadosModelo.ModeloSubCategoria;
import br.com.fenix.dominio.converter.CategoriaDeserializer;
import br.com.fenix.dominio.dto.CategoriaDTO;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@Data
public class SubCategoria extends MasterCategoria {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 878110273093281276L;
	

	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = false)	
//	@JoinColumn(updatable = false)
	@JsonDeserialize(using =  CategoriaDeserializer.class)
    public Categoria categoria;
    
    @Column(name="desp_fixa", nullable=true)
    private boolean desp_fixa; 
    
    @Column(name="imp_renda", nullable=true)
    private boolean imp_renda; 
	
 
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.setTipoLancamento(categoria.getTipoLancamento());
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
	@Override
    public CategoriaDTO categoria_DTO() {
    	return new CategoriaDTO(
    			this.getId(),
    			categoria.getDescricao().concat( " ->" ).concat(this.descricao) ,    			
    			tipoLancamento,
    			categoria.getId(),
				this.desp_fixa,
				this.imp_renda);
    }


	 
	 
}
