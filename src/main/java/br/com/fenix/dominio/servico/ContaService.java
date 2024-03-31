package br.com.fenix.dominio.servico;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.repositorio.dadosBasico.ContaRepositorio;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ContaService {

	private ContaRepositorio contaRP; 
	
	public void validar(Conta conta ) { 
		Optional<Conta> contaApelido = contaRP.findByApelido(conta.getApelido()); 
		
		System.out.println("Valida conta");
		if (contaApelido.isPresent() && conta.isNew() ) { 
		
			throw new NegocioException("Conta já cadastrada com esse apelido");
		}	
	}
/*	public Cliente buscar(Long clienteId) {
		return clienteRepository.findById(clienteId)
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
	}
*/	
}
