package br.com.fenix.abstrato.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.seguranca.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@AllArgsConstructor
@SuperBuilder
public abstract class EntidadeAbstrataAuto<ID> implements Persistable<ID>,  Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @Nullable
	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected ID  id;

//	@Transient
//	private boolean isNew ;
	  
	public EntidadeAbstrataAuto() {
		super();	
	}


   
   @Transient
	public boolean isNew() {
       return null == getId();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntidadeAbstrataAuto other = (EntidadeAbstrataAuto) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	} 
	   
	@Override
	public String toString() {
		return id.toString(); 
		
	}

//   @PrePersist 
//   @PostLoad
//   void markNotNew() {
//     this.isNew = false;
//   }
   
   
	
	
}
