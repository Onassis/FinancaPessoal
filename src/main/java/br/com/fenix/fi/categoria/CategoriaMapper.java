package br.com.fenix.fi.categoria;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.Categoria;

@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper extends GenericMapper<Categoria, CategoriaDTO> {
	   CategoriaMapper INSTANCE = Mappers.getMapper( CategoriaMapper.class );
	   
	   @Mapping(target = "categoria", ignore = true)
	   @Mapping(target = "desp_fixa", ignore = true)
	   @Mapping(target = "imp_renda", ignore = true)
	   CategoriaDTO convertToDto(Categoria entity);
  
	   @InheritInverseConfiguration
	   Categoria convertToEntity(CategoriaDTO dto) ;

	   Categoria updateEntity(@MappingTarget Categoria entity, CategoriaDTO dto) ;
	   
  
}
