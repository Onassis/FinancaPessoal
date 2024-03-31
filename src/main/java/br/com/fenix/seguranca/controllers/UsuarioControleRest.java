package br.com.fenix.seguranca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.abstrato.IControleRest;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.icontroller.IControleFavorecidoRest;
import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.repositorio.UsuarioRepositorio;
import groovy.console.ui.SystemOutputInterceptor;


@RestController
@RequestMapping("/usuario")
public class UsuarioControleRest extends ControleAbstratoRest<Usuario> implements  IControleRest<Usuario>   {

	
	public UsuarioControleRest(UsuarioRepositorio repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
	}
	

	@Override     
    public Usuario criar(Usuario entidade) {
    	// TODO Auto-generated method stub
		System.out.println("Cria usuario");
    	return super.criar(entidade);
    }  

}
