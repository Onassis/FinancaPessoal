package br.com.fenix.fi.conta;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.IServico;
import br.com.fenix.abstrato.ServicoAbstrato;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import lombok.AllArgsConstructor;

@Service
public class ContaService  extends ServicoAbstrato<Conta,Long> implements IServico<Conta,Long> {

	ContaRepositorio2 repositorio; 
	
	public ContaService(ContaRepositorio2 repositorio) {
		super(repositorio);
	}

	
	@Override
	public void antesDeSalvar(Conta entidade) throws NegocioException {
		Optional<Conta> contaApelido =    repositorio.findByApelido(entidade.getApelido()); 
		
		System.out.println("Valida conta");
		if (contaApelido.isPresent() && entidade.isNew() ) { 
		
			throw new NegocioException("Conta já cadastrada com esse apelido");
		}	
	}
//	public void validar(Conta conta ) { 
//		Optional<Conta> contaApelido = ((ContaRepositorio2) repositorio).findByApelido(conta.getApelido()); 
//		
//		System.out.println("Valida conta");
//		if (contaApelido.isPresent() && conta.isNew() ) { 
//		
//			throw new NegocioException("Conta já cadastrada com esse apelido");
//		}	
//	}
/*	public Cliente buscar(Long clienteId) {
		return clienteRepository.findById(clienteId)
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
	}
*/	
}
