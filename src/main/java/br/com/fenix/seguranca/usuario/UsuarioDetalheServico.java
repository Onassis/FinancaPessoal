package br.com.fenix.seguranca.usuario;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.fenix.api.exceptionhandle.RegistroJaExisteException;
import br.com.fenix.api.exceptionhandle.TokenNaoExisteException;

public interface UsuarioDetalheServico extends UserDetailsService {
	
	Usuario increver(Usuario usuario) throws RegistroJaExisteException;
	void SeExisteEmail(String username) throws RegistroJaExisteException;
	void SeExisteCpf(String cpf) throws RegistroJaExisteException; 
	String validateVerificationToken(String token) throws TokenNaoExisteException;

}
