package br.com.fenix.fi.lancamento;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.UUID;
import br.com.fenix.abstrato.dto.GenericMapper;


@Mapper(componentModel = "SPRING")
public interface LancDTOMapper  
//extends GenericMapper<Lancamento, LancDTO> 
{
	
//	   LancDTOMapper INSTANCE = Mappers.getMapper(LancDTOMapper.class );
//	   
//	
//	   LancDTO convertToDto(Lancamento entity);
//
//	   @br.com.fenix.abstrato.dto.ToEntity
//
//	   Lancamento convertToEntity(LancDTO dto) ;
//
//	   @br.com.fenix.abstrato.dto.ToEntity
//	   @Mapping(target = "detalheLancamento", ignore = true)
//	   @Mapping(target = "id", ignore = true)
//	   void updateEntity( LancDTO dto, @MappingTarget Lancamento entity) ;    
}


