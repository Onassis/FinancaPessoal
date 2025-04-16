package br.com.fenix.fi.subCategoria;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.dto.ToEntity;
import br.com.fenix.fi.categoria.CategoriaDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubCategoriaMapper extends Converter<SubCategoria, CategoriaDTO> {
	SubCategoriaMapper INSTANCE = Mappers.getMapper( SubCategoriaMapper.class );
	   

	 
	 SubCategoriaDTO ToDto(SubCategoria entity);

	@ToEntity
	SubCategoria ToEntity(CategoriaDTO dto) ;
	@ToEntity 
	void updateEntity(CategoriaDTO dto, @MappingTarget SubCategoria entity);
	 
	   

}
