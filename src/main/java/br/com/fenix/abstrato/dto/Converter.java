package br.com.fenix.abstrato.dto;

public interface Converter<E, D> {
	D createDto();
	E createEntity();
    D convertToDto(E entity);
    E convertToEntity(D dto);
    E updateEntity(E entity,D dto);
}
