package br.com.fenix.fi.categoria;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;

@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubCategoriaMapper extends GenericMapper<SubCategoria, CategoriaDTO> {

	  SubCategoriaMapper INSTANCE = Mappers.getMapper( SubCategoriaMapper.class );
		   
	 
	  CategoriaDTO convertToDto(SubCategoria entity);
	}
