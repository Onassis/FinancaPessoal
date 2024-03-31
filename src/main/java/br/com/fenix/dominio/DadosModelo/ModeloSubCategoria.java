package br.com.fenix.dominio.DadosModelo;


import br.com.fenix.abstrato.EntidadeAbstrata;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)

public class ModeloSubCategoria extends EntidadeAbstrata<Long>  {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	public ModeloCategoria ModeloCategoria;

	@Column(length = 40, nullable =  false)
	private String descricao;
	

    @Column(length = 2, nullable =  false, updatable = false)
    @Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;
    
    @Column(name="desp_fixa", nullable=true)
    private boolean desp_fixa; 
    
    @Column(name="imp_renda", nullable=true)
    private boolean imp_renda; 
   
}
