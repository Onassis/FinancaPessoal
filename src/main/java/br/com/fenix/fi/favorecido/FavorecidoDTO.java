package br.com.fenix.fi.favorecido;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.StringDeserializer;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.subCategoria.SubCategoria;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FavorecidoDTO extends EntidadeAbstrata<Long> {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	@JsonDeserialize(using = StringDeserializer.class) 
    @NotBlank
    @NotEmpty(message = "Nome deve ser informado.")
	@Column(length = 60,nullable = false)
	private String nome;

	@JsonDeserialize(using = ContaDeserializer.class)    
	private Conta conta;
	
	@JsonDeserialize( using = SubCategoriaDeserializer.class  )		
    private SubCategoria subCategoria;
	private long versao;
}
