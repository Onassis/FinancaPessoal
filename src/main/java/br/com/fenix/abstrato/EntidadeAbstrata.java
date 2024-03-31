package br.com.fenix.abstrato;

import java.io.Serializable;


import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class EntidadeAbstrata<P> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntidadeAbstrata() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id @Nullable
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(updatable = false)
	private P  id;

	@Transient 
	public boolean isNew() {
		return null == getId();
	} 
   
	
	
}
