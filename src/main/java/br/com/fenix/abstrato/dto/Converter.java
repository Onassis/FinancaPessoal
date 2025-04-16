package br.com.fenix.abstrato.dto;

import org.mapstruct.MappingTarget;

public interface Converter<E, D> {

    
	D ToDto(E entity);
    E ToEntity(D dto);
    
    void updateEntity(D dto, @MappingTarget E entity);
}
