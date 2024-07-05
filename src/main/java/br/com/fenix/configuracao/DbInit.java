package br.com.fenix.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.ModeloCategoriaRepositorio;
import br.com.fenix.seguranca.usuario.*;
import br.com.fenix.seguranca.usuario.UsuarioControleRest;
import br.com.fenix.seguranca.usuario.Usuario.Role;

import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {
	@Autowired
    private UsuarioControleRest userRepository;
//	@Autowired
//    private ModeloCategoriaRepositorio ModCadRepository;
        
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	

    public DbInit(UsuarioControleRest userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        
    	
//    	  Usuario onassis  = new Usuario("onassis.tavares@gmail.com", "Onassis", "Tavares" ,passwordEncoder.encode("senha") ); 
    	
//    	System.out.println("Inicio");
    	
 //       Usuario onassis  = new Usuario("onassis.tavavares@gmail.com", "Onassis", "Tavares" ,passwordEncoder.encode("senha") ); 

//        onassis.addRole(Role.ADMIN);
        
//        System.out.println(onassis);
        
//        userRepository.criar(onassis);
        
  
    }
}
