package br.com.fenix.fi.conta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoConta;
import lombok.AllArgsConstructor;

@Service
public class ContaServico extends ServicoAbstrato<ContaRepositorio,Conta,Long> implements IServico<Conta,Long> {

//   @Autowired
//   private ContaRepositorio contaRp;
	
   
	public ContaServico(ContaRepositorio repositorio) {
		super(repositorio);
	}
//   
//	public CrudRepository<Conta,Long> getRp() {
//		return this.contaRp;
//	}
	public List<Option>  listaDeContas(TipoConta tipoConta) {
		   List<Option> options = repositorio.findByTipoContaOrderByApelidoAsc(tipoConta).stream()    
				.map(conta -> new Option(conta.getId(), conta.getAjuda()))
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

	
}
