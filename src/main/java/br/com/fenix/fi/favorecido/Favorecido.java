package br.com.fenix.fi.favorecido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.MoedaDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.converter.rest.StringDeserializer;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.moeda.Moeda;
import br.com.fenix.fi.subCategoria.SubCategoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name="favorecido" , indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
