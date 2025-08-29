package br.com.fenix.fi.conta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.favorecido.FavorecidoMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

@Service
public class ContaServico extends ServicoAbstratoDTO<Conta,ContaDTO, UUID> implements IServicoDTO<Conta,ContaDTO,UUID> {

   @Autowired
   private ContaRepositorio contaRp;
	@Autowired
	private ContaMapper converter;
//	
//   private GenericConverter<Conta, ContaDTO> converter; 
//   
	public ContaServico() {
    	super();
	//	this.converter = new GenericConverter<>(Conta.class, ContaDTO.class);		
	}
	@Override
	public ContaMapper getConverter() {

		return converter;
	}	
	public List<Option>  listaDeContas(TipoConta tipoConta) {
		   List<Option> options = contaRp.findByTipoContaOrderByApelidoAsc(tipoConta).stream()    
				.map(conta -> new Option(conta.getId().toString(), conta.getAjuda()))
	            .collect(Collectors.toList());
			return options;
			
	}

	@Override
	public void handleException(OperacaoDB op,Exception e) throws Exception {
		
		  if ( e.getMessage().contains("contaapelido")) {
			  throw new NegocioException("Já existe um conta/cartão com esse apelido");				  			  
		  }
		  if (op == OperacaoDB.DEL) { 
			  throw new NegocioException("Conta/Cartão possui lançamento e não pode ser excluida");
		  }
		  
	
 	  throw e ;		
	}
	@Override
	public JpaRepository<Conta, UUID> getRp() {
		return contaRp;
	}




	
}
