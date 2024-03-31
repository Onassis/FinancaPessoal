package br.com.fenix.abstrato;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.seguranca.modelo.Usuario;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"dtCriacao", "dtAlteracao","criadoPor","alteradoPor"},
        allowGetters = true
)

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public abstract class EntidadeAuditavel<P> extends EntidadeAbstrata<P>   {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
    @CreatedBy
    @ManyToOne(
             cascade = { CascadeType.DETACH  }, 
             fetch = FetchType.LAZY ,
             optional = false              
    )
    @JoinColumn(updatable = false)
    @JsonIgnore
    private Usuario criadoPor;

    @CreatedDate
    @Column(name = "dt_criacao", nullable = false, updatable = false)
    private Instant dtCriacao;
	
	
	@LastModifiedBy
    @ManyToOne(
            cascade = { CascadeType.DETACH }, 
            fetch = FetchType.LAZY ,
            optional = false
             
   )
	@JsonIgnore
    private Usuario alteradoPor;
	

    @Column(name = "dt_alteracao")
    @LastModifiedDate
    private Instant dtAlteracao;
    
	@Version
	private long versao;
	
	public boolean usuarioIgual ( Usuario usuario) {
	    	
		return this.criadoPor.equals(usuario); 
	 }

	public EntidadeAuditavel(Usuario criadoPor) {
		super();
		this.criadoPor = criadoPor;
	}
	
}

