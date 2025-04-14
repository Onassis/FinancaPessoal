package br.com.fenix.abstrato.dto;

import org.mapstruct.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


public interface GenericMapper<E, D> extends Converter<E, D> {
    D convertToDto(E entity);
    @ToEntity
    E convertToEntity(D dto);
    @ToEntity
    E updateEntity(@MappingTarget E entity,D dto);
 

}