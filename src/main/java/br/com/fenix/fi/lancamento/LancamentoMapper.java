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
	   
	 
	    @Mapping(source = "id", target = "lancamentoId")
	    @Mapping(source = "informacao", target = "lancamentoInformacao")
	    @Mapping(source = "observacao", target = "lancamentoObservacao")
	    @Mapping(source = "dataDoc", target = "lancamentoDataDoc")
//	    @Mapping(source = "dataVenc", target = "dataVenc")
//	    @Mapping(source = "tipoLancamento", target = "tipoLancamento")
	    @Mapping(source = "tipoOperacao", target = "lancamentoTipoOperacao")
	    @Mapping(source = "favorecido", target = "lancamentoFavorecido")
//	    @Mapping(source = "contaLancamento", target = "contaLancamento")
//	    @Mapping(source = "chaveBanco", target = "chaveBanco")
//	    @Mapping(source = "refBanco", target = "refBanco")
//	    @Mapping(source = "contaDestino", target = "contaDestino")
	    @Mapping(source = "subCategoria", target = "lancamentoSubCategoria")
	    @Mapping(source = "nroPrestacao", target = "lancamentoNroPrestacao")
	    @Mapping(source = "nroInicialPrestacao", target = "lancamentoNroInicialPrestacao")
	    @Mapping(source = "total", target = "lancamentoTotal")
//	    @Mapping(source = "valor", target = "valor")
//	    @Mapping(source = "saldo", target = "saldo")
//	    @Mapping(source = "credito", target = "credito")
//	    @Mapping(source = "debito", target = "debito")
//	    @Mapping(source = "conciliado", target = "conciliado")
	    @Mapping(source = "criadoPor", target = "criadoPor")
	   LancamentoDTO convertToDto(Lancamento entity);


	    @Mapping(source = "lancamentoId", target = "id")
	    @Mapping(source = "lancamentoInformacao", target = "informacao")
	    @Mapping(source = "lancamentoObservacao", target = "observacao")
	    @Mapping(source = "lancamentoDataDoc", target = "dataDoc")
//	    @Mapping(source = "dataVenc", target = "dataVenc")
//	    @Mapping(source = "tipoLancamento", target = "tipoLancamento")
	    @Mapping(source = "lancamentoTipoOperacao", target = "tipoOperacao")
	    @Mapping(source = "lancamentoFavorecido", target = "favorecido")
//	    @Mapping(source = "contaLancamento", target = "contaLancamento")
//	    @Mapping(source = "chaveBanco", target = "chaveBanco")
//	    @Mapping(source = "refBanco", target = "refBanco")
//	    @Mapping(source = "contaDestino", target = "contaDestino")
	    @Mapping(source = "lancamentoSubCategoria", target = "subCategoria")
	    @Mapping(source = "lancamentoNroPrestacao", target = "nroPrestacao")
	    @Mapping(source = "lancamentoNroInicialPrestacao", target = "nroInicialPrestacao")
	    @Mapping(source = "lancamentoTotal", target = "total")
//	    @Mapping(source = "valor", target = "valor")
//	    @Mapping(source = "saldo", target = "saldo")
//	    @Mapping(source = "credito", target = "credito")
//	    @Mapping(source = "debito", target = "debito")
//	    @Mapping(source = "conciliado", target = "conciliado")
//	    @Mapping(source = "criadoPor", target = "criadoPor")
	   @br.com.fenix.abstrato.dto.ToEntity
	   	Lancamento convertToEntity(LancamentoDTO dto) ;

//	   @Mapping(source = "conta", target = "conta")
//	   @Mapping(source = "versao", target = "versao")
	   void updateEntity( LancamentoDTO dto, @MappingTarget Lancamento entity) ;    
}


