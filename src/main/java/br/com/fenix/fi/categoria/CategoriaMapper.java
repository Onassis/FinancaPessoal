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
	   
	   @Mapping(target = "desp_fixa", ignore = true)
	   @Mapping(target = "imp_renda", ignore = true)
	   CategoriaDTO ToDto(Categoria entity);
  
	   @InheritInverseConfiguration
	   Categoria ToEntity(CategoriaDTO dto) ;

	   void updateEntity(CategoriaDTO dto, @MappingTarget Categoria entity) ;
	   
  
}
