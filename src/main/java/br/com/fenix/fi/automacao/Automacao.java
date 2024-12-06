package br.com.fenix.fi.automacao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.dominio.converterRest.ContaDeserializer;
import br.com.fenix.dominio.converterRest.FavorecidoDeserializer;
import br.com.fenix.dominio.converterRest.StringDeserializer;
import br.com.fenix.dominio.converterRest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.categoria.SubCategoria;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.seguranca.usuario.Usuario;
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
	/*@Column(length = 60)*/
	@Column(columnDefinition = "TEXT")
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
	
	@Transient
	private String[] criterios; 
	
	public Automacao() {
		super();
		ordem = 0;
	}
	public String[] getCriterios() {
	    if (criterio != null && !criterio.isEmpty()) {
	        return criterio.split(";");
	    }
	    return new String[0];
	}
	public void setCriteriosFromLista(String[] criteriosArray) {
        if (criteriosArray != null && criteriosArray.length > 0) {
            this.criterio = String.join(",", criteriosArray);
        } else {
            this.criterio = "";
        }
    }
}
