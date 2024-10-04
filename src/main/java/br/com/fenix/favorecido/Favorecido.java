package br.com.fenix.favorecido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.fenix.abstrato.EntidadeAbstrata;
import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converterRest.ContaDeserializer;
import br.com.fenix.dominio.converterRest.StringDeserializer;
import br.com.fenix.dominio.converterRest.SubCategoriaDeserializer;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.fi.conta.Conta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="favorecido" , indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Favorecido extends EntidadeAuditavel<Long>  {
	private static final long serialVersionUID = -4594764148388431106L;

	/**
	 * 
	 */
	@JsonDeserialize(using = StringDeserializer.class) 
    @NotBlank
    @NotEmpty(message = "Nome deve ser informado.")
	@Column(length = 60,nullable = false)
	private String nome;

	@JsonDeserialize(using = ContaDeserializer.class)    
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY , optional = true)	
	private Conta conta;
	
	@JsonDeserialize( using = SubCategoriaDeserializer.class  )
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY , optional = true)	
    private SubCategoria subCategoria;

	
}
