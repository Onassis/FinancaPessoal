package br.com.fenix.fi.conta;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;
import br.com.fenix.abstrato.dto.ToEntity;


@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContaMapper extends GenericMapper<Conta, ContaDTO> {

	ContaMapper INSTANCE = Mappers.getMapper( ContaMapper.class );
	   
   ContaDTO convertToDto(Conta entity);
   @ToEntity 
   Conta convertToEntity(ContaDTO dto) ;
   @ToEntity 
   Conta updateEntity(@MappingTarget Conta entity, ContaDTO dto) ; 

}
