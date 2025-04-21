package br.com.fenix.abstrato.base;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class EntidadeAbstrata<ID> implements Persistable<ID>,  Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Transient
//	private boolean isNew ;
	  
	public EntidadeAbstrata() {
		super();	
	}

	@Id @Nullable
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(updatable = false)
	protected ID  id;

   
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
		EntidadeAbstrata other = (EntidadeAbstrata) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	} 
	   
   
//   @PrePersist 
//   @PostLoad
//   void markNotNew() {
//     this.isNew = false;
//   }
   
   
	
	
}
