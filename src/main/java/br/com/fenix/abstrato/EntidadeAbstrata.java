package br.com.fenix.abstrato;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class EntidadeAbstrata<ID> implements Persistable<ID>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private boolean isNew = true;
	  
	public EntidadeAbstrata() {
		super();	
	}

	@Id @Nullable
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(updatable = false)
	private ID  id;

   @Override
	public boolean isNew() {
	   return isNew;
//		return null == getId();
	} 
   
   @PrePersist 
   @PostLoad
   void markNotNew() {
     this.isNew = false;
   }
   
   
	
	
}
