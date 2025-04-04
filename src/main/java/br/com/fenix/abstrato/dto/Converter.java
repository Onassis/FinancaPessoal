package br.com.fenix.abstrato.dto;

import org.mapstruct.MappingTarget;

public interface Converter<E, D> {
//	D createDto();
//	E createEntity();
    D convertToDto(E entity);
    E convertToEntity(D dto);
    E updateEntity(@MappingTarget E entity,D dto);
}
