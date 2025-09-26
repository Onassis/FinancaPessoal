package br.com.fenix.abstrato.base;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.uuid.GeneratedUuidV7;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.seguranca.usuario.Usuario;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public abstract class Auditavel<ID>  implements Persistable<ID>, Comparable<ID>, Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

    @ManyToOne(
             cascade = { CascadeType.DETACH  }, 
             fetch = FetchType.LAZY ,
             optional = false              
    )
    @JoinColumn(updatable = false)
    @JsonIgnore
    @CreatedBy
    protected Usuario criadoPor;

  
    @Column(name = "dt_criacao", nullable = false, updatable = false)
    @CreatedDate
    protected Instant dtCriacao;
	
    
	@JsonIgnore

    @ManyToOne(
            cascade = { CascadeType.DETACH }, 
            fetch = FetchType.LAZY ,
            optional = true
             
   )	
	@LastModifiedBy
	protected Usuario alteradoPor;
	
    @Column(name = "dt_alteracao",	nullable = true)
    @LastModifiedDate
    protected Instant dtAlteracao;
    
	@Version
	protected long versao;
	
	@Transient
	protected String ajuda="" ;
	
	
	public Auditavel(Usuario criadoPor) {
		super();
		this.criadoPor = criadoPor;
	}
	
   
   @Transient
	public boolean isNew() {
       return null == getId();
	}

}

