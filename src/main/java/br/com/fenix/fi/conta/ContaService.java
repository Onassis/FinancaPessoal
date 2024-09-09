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
public class ContaService  extends ServicoAbstrato<ContaRepositorio,Conta,Long> implements IServico<Conta,Long> {


	
	public ContaService(ContaRepositorio repositorio) {
		super(repositorio);
	}

	@Override
	public void antesDeSalvar(Conta entidade) throws NegocioException {
		System.out.println("Valida conta");
		Optional<Conta> contaApelido =    repositorio.findByApelido(entidade.getApelido()); 

		if (contaApelido.isPresent() && entidade.isNew() ) { 
		
			throw new NegocioException("Conta/Cartão já cadastrada com esse apelido");
		}	
	}

	@Override
	public void antesDeAlterar(Conta entidade) throws NegocioException {
		Optional<Conta> contaApelido =    repositorio.findByApelido(entidade.getApelido()); 
		if (contaApelido.get().getApelido() ==  entidade.getApelido() && contaApelido.get().getId() != entidade.getId()) { 		
			throw new NegocioException("Conta/Cartão não pode ser alterada para esse apelido");
		}	
		
	}

	@Override
	public void depoisDeAlterar(Conta entidade) throws NegocioException {
		
	}

	@Override
	public void depoisDeSalvar(Conta entidade) throws NegocioException {
	
	}

	@Override
	public void antesDeExcluir(Long id) throws NegocioException {
		System.out.println("Antes de Excluir ->  Conta");
		boolean existeLancamento = repositorio.existsByContaLancamento (id);
		if (existeLancamento) {
			throw new NegocioException("Conta/Cartão possui lançamento e não pode ser excluida");
		}

		
	}
	
}
