package br.com.fenix.fi.favorecido;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;

@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface FavorecidoMapper extends GenericMapper<Favorecido, FavorecidoDTO> {
   FavorecidoMapper INSTANCE = Mappers.getMapper( FavorecidoMapper.class );
   
   @Mapping(source = "conta", target = "conta")
   @Mapping(source = "versao", target = "versao")
   FavorecidoDTO convertToDto(Favorecido entity);
      

   
   
	
}
