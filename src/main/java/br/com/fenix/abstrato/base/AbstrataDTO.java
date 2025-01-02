package br.com.fenix.abstrato.base;

import org.springframework.data.domain.Persistable;

public abstract class AbstrataDTO<T extends Persistable<ID>,ID> extends EntidadeAbstrata<ID> { 
	
	/**
     * 
     */
	public abstract class AbstractDTO<T> {

	    // Construtor abstrato para ser implementado pelas subclasses
	    public AbstractDTO(T entity) {
	        if (entity == null) {
	            throw new IllegalArgumentException("Entity cannot be null");
	        }
	        EntidadeToDTO(entity);
	    }

	    /**
	     * Método abstrato que deve ser implementado pelas subclasses
	     * para mapear os dados da entidade para o DTO.
	     *
	     * @param entity a entidade de origem
	     */
	    abstract void EntidadeToDTO(T entidade);
	    abstract T DTOtoEntidade();
	}

}
