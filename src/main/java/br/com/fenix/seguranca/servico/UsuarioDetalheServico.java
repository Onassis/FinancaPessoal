package br.com.fenix.seguranca.servico;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.modelo.UsuarioDto;

public interface UsuarioDetalheServico extends UserDetailsService {
	
	Usuario increver(UsuarioDto usuarioDto) throws UsernameNotFoundException;
	void SeExiste(String username) throws UsernameNotFoundException;

}
