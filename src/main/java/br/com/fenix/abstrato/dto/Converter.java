package br.com.fenix.abstrato.dto;


public interface Converter<E, D> {

    
	D ToDto(E entity);
    E ToEntity(D dto);    
    void updateEntity(D dto, E entity);
}
