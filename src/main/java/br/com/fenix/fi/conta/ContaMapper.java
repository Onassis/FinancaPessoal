package br.com.fenix.fi.conta;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.UUID;

import br.com.fenix.abstrato.dto.GenericMapper;
import br.com.fenix.abstrato.dto.ToEntity;


import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContaMapper extends GenericMapper<Conta, ContaDTO> {

	ContaMapper INSTANCE = Mappers.getMapper( ContaMapper.class );
	   
   ContaDTO convertToDto(Conta entity);
   @ToEntity 
   Conta convertToEntity(ContaDTO dto) ;
   @ToEntity 
   void updateEntity(ContaDTO dto, @MappingTarget Conta entity ) ; 
   

}
