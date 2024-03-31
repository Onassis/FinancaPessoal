package br.com.fenix.dominio.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converter.StringDeserializer;
import br.com.fenix.dominio.modelo.Orcamento;
import br.com.fenix.dominio.modelo.ValorAno;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class OrcamentoDTO  implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
    
	private Long  id;

    
	@JsonDeserialize(using = StringDeserializer.class)		
	private String descricao;
	
	

	@Column(nullable = false)
	private int ano; 
	

    private Categoria categoria;
    
  
    private SubCategoria subCategoria;
    
    @Embedded
	private ValorAno orcamentoMes; 
	
	public OrcamentoDTO(@NotNull int ano, SubCategoria subCategoria) {
		super();
		this.ano = ano;
		this.subCategoria = subCategoria;
		this.categoria  = subCategoria.getCategoria();		
		this.descricao = subCategoria.getCategoria().getDescricao()
				.concat( " ->" )
				.concat(subCategoria.getDescricao()) ;
	}

	public OrcamentoDTO(@NotNull int ano, Categoria categoria) {
		super();
		this.ano = ano;
		this.categoria = categoria;
		this.descricao = categoria.getDescricao();
	}

	public OrcamentoDTO(Orcamento orcamento) {
		super();
		this.ano = orcamento.getAno();
		this.subCategoria = orcamento.getSubCategoria();
		this.categoria  = subCategoria.getCategoria();		
		this.descricao = subCategoria.getCategoria().getDescricao()
				.concat( " ->" )
				.concat(subCategoria.getDescricao()) ;
	}


    
	public void setSubCategoria (SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
		this.categoria = subCategoria.getCategoria();
		this.descricao = subCategoria.getCategoria().getDescricao()
						.concat( " ->" )
						.concat(subCategoria.getDescricao()) ;
	}
}
