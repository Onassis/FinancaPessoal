package br.com.fenix.seguranca.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fenix.api.exceptionhandle.RegistroJaExisteException;
import br.com.fenix.api.exceptionhandle.TokenNaoExisteException;
import br.com.fenix.email.EmailModel;
import br.com.fenix.email.EmailService;
import br.com.fenix.seguranca.usuario.Usuario.Role;


@Service
public class UsuarioServicoImp implements UsuarioDetalheServico {

	    private final UsuarioRepositorio UsuarioRp ;
	   
		@Autowired 
		PasswordEncoder passwordEncode; 
		@Autowired 
		EmailService emailService;
		

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
		public Usuario increver(Usuario usuario) throws RegistroJaExisteException {
			SeExisteEmail(usuario.getEmail()); 
			SeExisteCpf(usuario.getCpf());
			usuario.setPassword( passwordEncode.encode(usuario.getPassword()));
			usuario.setEnabled(1); 
			
			usuario.addRole(Role.USER);
				
			usuario.setEnabled(0);

	        // Generate verification token and send email
	        String token = UUID.randomUUID().toString();
	        
	        usuario.setVerificationToken(token);
	        	

	        String confirmationUrl =  "http://localhost:8080/FinancaPessoal/verify-email?token=" + token;
	        
	        usuario = UsuarioRp.save(usuario) ; 
	        
	        EmailModel emailModel = new EmailModel( 
	        		"Usuario", 
	        		"onassis.tavares@gmail.com",
	        		usuario.getEmail(), 
	        		"Email Verification", 
	        		"Click the link to verify your email: " +  confirmationUrl
	        		);
	        
	     
			emailService.sendEmail(emailModel);

	        return usuario;
		}

		public String validateVerificationToken(String token) throws TokenNaoExisteException {
			
			Usuario usuario = UsuarioRp.findByVerificationToken(token).get(); 	
//					 .orElseThrow(new TokenNaoExisteException(token));
	        
		

	        usuario.setEnabled(1); 
	        
	        UsuarioRp.save(usuario);
	        return "valid";
	    }
		@Override
		public void SeExisteEmail(String username) throws RegistroJaExisteException {
			// TODO Auto-generated method stub
			if (UsuarioRp.findByEmail(username).isPresent()) {
				System.out.println("SeExiste " + username);
					new RegistroJaExisteException("Usuario já cadastrado : " + username);
			}
		}

		@Override
		public void SeExisteCpf(String cpf) throws RegistroJaExisteException {
			if (UsuarioRp.findByCpf(cpf).isPresent()) {
				System.out.println("SeExiste " + cpf);
					new RegistroJaExisteException("Usuario já cadastrado : " + cpf);
			}
			
		}
}
		

