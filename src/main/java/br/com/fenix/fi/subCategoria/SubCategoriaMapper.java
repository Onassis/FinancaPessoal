package br.com.fenix.fi.subCategoria;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.categoria.CategoriaDTO;




@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface SubCategoriaMapper extends Converter< SubCategoria, SubCategoriaDTO> {

	SubCategoriaMapper INSTANCE = Mappers.getMapper( SubCategoriaMapper.class );

    @Mapping(target = "categoria", source = "categoria")
	SubCategoriaDTO ToDto(SubCategoria entity);
	
	@InheritInverseConfiguration
    @Mapping(target = "categoria", source = "categoria")
	SubCategoria ToEntity(SubCategoriaDTO dto) ;

	@Mapping(target = "categoria",ignore = true)
	void updateEntity(SubCategoriaDTO dto, @MappingTarget SubCategoria entity) ;
		
//	@Mapping(source = "categoria.id", target = "id")
//	@Mapping(source = "categoria.descricao", target = "descricao")
//	@Mapping(source = "categoria.tipoCategoria", target = "tipoCategoria")
//	@Mapping(source = "categoria.tipoLancamento", target = "tipoLancamento")
//	@Mapping(source = "categoria.versao", target = "versao")	
//    Categoria mapperCategoria( CategoriaDTO categoria);
	 

	   

}
