package br.com.fenix.fi.lancamento;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;


@Mapper(componentModel = "spring")
public interface DetalheLancMapper  extends GenericMapper<DetalheLancamento, DetalheLancDTO> {

	DetalheLancMapper INSTANCE = Mappers.getMapper(DetalheLancMapper.class );
   	
	

	 @br.com.fenix.abstrato.dto.ToEntity	 
	 @Mapping(source = "lancamento" , target = "lancDTO")
	 DetalheLancDTO convertToDto(DetalheLancamento entity);

	 @br.com.fenix.abstrato.dto.ToEntity
	 @Mapping(source = "lancDTO" , target = "lancamento")
	 DetalheLancamento convertToEntity(DetalheLancDTO dto) ;
	 
	 @Mapping(target = "lancamento", ignore = true)
	 @Mapping(target = "id", ignore = true)
	 void updateEntity( DetalheLancDTO dto, @MappingTarget DetalheLancamento entity) ;    
}