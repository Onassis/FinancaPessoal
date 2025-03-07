package br.com.fenix.abstrato.base;

import org.springframework.data.domain.Persistable;

public abstract class AbstrataDTO<T extends Persistable<ID>,ID> extends EntidadeAbstrata<ID> implements IAbstractoDTO{ 
	
	/**
	 * @return 
	 * @return 
     * 
     */

		/**
	     * Método abstrato que deve ser implementado pelas subclasses
	     * para mapear os dados da entidade para o DTO.
	     *
	     * @param entity a entidade de origem
	     */
	
//	    abstract DTO EntidadeToDTO(T entidade);
//	    abstract T DTOtoEntidade();

	    public AbstrataDTO(T entidade) {
			super();
    	}
}
