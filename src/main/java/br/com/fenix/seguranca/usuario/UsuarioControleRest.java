package br.com.fenix.seguranca.usuario;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.abstrato.controle.ControleAbstratoRest;
import br.com.fenix.abstrato.controle.IControleRest;
import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.IControleFavorecidoRest;
import groovy.console.ui.SystemOutputInterceptor;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuario")
public class UsuarioControleRest extends ControleAbstratoRest<Usuario> implements  IControleRest<Usuario>   {

	@Autowired
	UsuarioServicoImp usuarioSv;

	
	public UsuarioControleRest(UsuarioRepositorio repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
	}
	

	@Override     
    public Usuario criar(@Valid Usuario entidade) {
    	// TODO Auto-generated method stub
		System.out.println("Usuario controle -> Cria usuario");
		return usuarioSv.increver(entidade);
    }  
	

}
