package br.com.fenix.seguranca.servico;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fenix.api.exceptionhandle.RegistroJaExisteException;
import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.modelo.UsuarioDto;
import br.com.fenix.seguranca.repositorio.UsuarioRepositorio;


@Service
public class UsuarioServicoImp implements UsuarioDetalheServico {

	    private final UsuarioRepositorio UsuarioRp ;
	   

	    public UsuarioServicoImp(UsuarioRepositorio usuarioRp) {
			UsuarioRp = usuarioRp;

		}

		@Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
		   System.out.println("login : " + username);  
			Optional<Usuario> usuario =  UsuarioRp.findByEmail(username);
			
			if (usuario.isEmpty()) {
				          System.out.println("não encontro" + username);  
						 new UsernameNotFoundException("User Not Found with username: " + username) ;
			}		
		    System.out.println(usuario.get());  
			return usuario.get();

					 
	    }
		
/*		public Usuario save(UsuarioDto usuarioDto) {
			Usuario user = new Usuario(); 
		return UsuarioRp.save(user);
		}
*/		
		@Override
		public Usuario increver(UsuarioDto usuarioDto) throws UsernameNotFoundException {
			Usuario user = usuarioDto.toUsuario(); 
			return UsuarioRp.save(user);			
		}

		@Override
		public void SeExiste(String username) throws RegistroJaExisteException {
			// TODO Auto-generated method stub
			if (UsuarioRp.findByEmail(username).isPresent()) {
				System.out.println("SeExiste " + username);
					new RegistroJaExisteException("Usuario já cadastrado : " + username);
			}
		}
}
		

