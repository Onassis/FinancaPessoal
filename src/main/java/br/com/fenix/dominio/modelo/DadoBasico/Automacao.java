package br.com.fenix.dominio.modelo.DadoBasico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.fenix.abstrato.EntidadeAbstrata;
import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converter.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.StringDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="automacao" , indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
@Getter
@Setter
@ToString
public class Automacao extends EntidadeAuditavel<Long>{
	private static final long serialVersionUID = -4594764148388431106L;

	/**
	 * 
	 */
	@Column(nullable = false)
	private int ordem;
	
	@JsonDeserialize(using = StringDeserializer.class) 
    @NotBlank
	@Column(length = 60)
	private String criterio;
	
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoOperacao TipoOperacao;
    
    @JsonDeserialize( using = FavorecidoDeserializer.class  )
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY  )
	@JsonBackReference
	private Favorecido favorecido;

	@JsonDeserialize( using = SubCategoriaDeserializer.class  )
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY , optional = true)	
    private SubCategoria subCategoria;
	
	@JsonDeserialize(using = ContaDeserializer.class)    
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY , optional = true)	
	private Conta contaTransferencia;
	
	public Automacao() {
		super();
		ordem = 0;
	}
	
}
