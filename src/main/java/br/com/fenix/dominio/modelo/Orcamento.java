package br.com.fenix.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.util.ValorAno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity 
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class Orcamento  extends EntidadeAuditavel<Long> {
	private static final long serialVersionUID = 1L;
    
	@NotNull
	@Column(nullable = false)
	private int ano; 
	
    @ManyToOne (fetch = FetchType.LAZY )
    private Categoria categoria;
    
    @ManyToOne (fetch = FetchType.LAZY )
    private SubCategoria subCategoria;
    

	private ValorAno orcamentoMes; 
    
	public void setSubCategoria (SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
		this.categoria = subCategoria.getCategoria();
	}
}
