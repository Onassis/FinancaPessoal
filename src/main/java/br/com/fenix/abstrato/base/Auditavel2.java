package br.com.fenix.abstrato.base;

import java.time.Instant;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import br.com.fenix.seguranca.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(AuditingEntityListener.class)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Auditavel2 {

  
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
	

    @Column(name = "dt_alteracao")
    @LastModifiedDate
    protected Instant dtAlteracao;
    
	@Version
	protected long versao;
	
	@Transient
	protected	 String ajuda="" ;
	
	public boolean usuarioIgual ( Usuario usuario) {
	    	
		return this.criadoPor.equals(usuario); 
	 }


}
