package br.com.fenix.fi.lancamento;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.fenix.abstrato.dto.GenericMapper;


@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface LancamentoMapper  extends GenericMapper<Lancamento, LancamentoDTO> {
	   LancamentoMapper INSTANCE = Mappers.getMapper(LancamentoMapper.class );
	   

	   LancamentoDTO convertToDto(Lancamento entity);

	   @br.com.fenix.abstrato.dto.ToEntity
	   Lancamento convertToEntity(LancamentoDTO dto) ;

	   void updateEntity( LancamentoDTO dto, @MappingTarget Lancamento entity) ;    
}


