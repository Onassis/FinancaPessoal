package br.com.fenix.fi.favorecido;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.modelo.Option;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@Service
public class FavorecidoServico  extends ServicoAbstratoDTO<Favorecido,FavorecidoDTO, Long> implements IServicoDTO<Favorecido,FavorecidoDTO,Long> {

	@Autowired
	FavorecidoRepositorio repositorio;
	@Autowired
	private FavorecidoMapper converter;
	
	public FavorecidoServico(EntityManagerFactory emf) {
    	super(emf);
	}
	@Override
	public JpaRepository<Favorecido, Long> getRp() {
		
		return repositorio;
	}

	public List<Option>  listaDeFavorecido() {
		   List<Option> options = repositorio.findAll()
		   		.stream()    
				.map(favorecido  -> new Option(favorecido.getId(), favorecido.getNome()))
	            .collect(Collectors.toList());
			return options;
			
	}	

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
	
		  if ( e instanceof 	DataIntegrityViolationException) { 
			  throw new NegocioException("Favorecido possui lançamento e não pode ser excluida");
		  }		  
	
 	  throw e ;	
	}

	@Override
	public FavorecidoMapper getConverter() {	
		return converter;
	}


}