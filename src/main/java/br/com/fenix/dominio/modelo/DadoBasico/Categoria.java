package br.com.fenix.dominio.modelo.DadoBasico;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.DadosModelo.ModeloSubCategoria;

@EntityListeners(AuditingEntityListener.class)
@Entity

//@DiscriminatorValue( value="CT" )
@EqualsAndHashCode(callSuper=true)
@Data
@NoArgsConstructor
public class Categoria extends MasterCategoria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @JsonIgnore    

    @Column(nullable = false)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<SubCategoria> subCategoria ;
    
    public Categoria ( ModeloCategoria modeloCategoria) { 
    	   super();
    	   subCategoria = new ArrayList<>();
    	   this.descricao = modeloCategoria.getDescricao();
           this.tipoLancamento = modeloCategoria.getTipoLancamento();
    	  
    	   
           for (ModeloSubCategoria modeloSubCat: modeloCategoria.getSubModeloCategoria()) {
        	   subCategoria.add( new SubCategoria(this, modeloSubCat)); 
           }           
           
    }

	@Override
	public String toString() {
		return "Categoria [descricao=" + descricao + ", tipoLancamento="
				+ tipoLancamento + ", inativo=" + inativo + "]";
	}

	   

}
