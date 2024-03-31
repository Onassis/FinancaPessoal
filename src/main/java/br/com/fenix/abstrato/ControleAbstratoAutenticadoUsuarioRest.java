package br.com.fenix.abstrato;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.api.exceptionhandle.EntidadeNaoEncontratException;
import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;

public abstract class ControleAbstratoAutenticadoUsuarioRest<T> implements IControleRest<T>{
	
	 private final GenericRepositoryAutenticado<T> repositorio;
	 private final Optional<Usuario> usuario= UtilSerguranca.currentUser();

	    public ControleAbstratoAutenticadoUsuarioRest(GenericRepositoryAutenticado<T> repositorio) {
 	        this.repositorio = repositorio;
 	 //       if ( UtilSerguranca.currentUser().isPresent() )
// 	        	this.usuario = UtilSerguranca.currentUser().get();
 	        }
	    @Override
	 	public String nomeEntidade(T entidade) {
	    	 return entidade.getClass().getSimpleName().toLowerCase(); 
	    }
	    @Override
		public String nomeCadastro(T entidade) {
	    	String nomeEntidade = nomeEntidade(entidade); 
	    	return nomeEntidade.concat("/cad_").concat(nomeEntidade);	    
	    }
	    @Override
	    public String nomeListar(T entidade) {
	    	String nomeEntidade = entidade.getClass().getSimpleName().toLowerCase(); 
	    	return nomeEntidade.concat("/listar_").concat(nomeEntidade);	 
	    }	
	    @Override
	    @GetMapping("/cadastrar")  	
	    public ModelAndView cadastrar(T entidade) {
	       	return new ModelAndView(nomeCadastro(entidade));
	    }
	    @Override
	    @GetMapping("/editar/{id}")  
		public ModelAndView atualizarView(@PathVariable long id) {    	
			T entidade = buscarPorId(id); 
			
			return new ModelAndView(nomeCadastro(entidade),nomeEntidade(entidade),entidade) ;		  			  
		}  	  
	    @Override
		@GetMapping("/listar")  
		public ModelAndView listarView(T entidade) {	
	    	System.out.println("listarView");
	    	System.out.println(this.usuario.get());
			Iterable<T> dados = repositorio.findByCriadoPor(this.usuario.get());		
			return new ModelAndView(nomeListar(entidade),nomeEntidade(entidade),dados) ;		  			  
		}	
	    @Override
	    @ResponseStatus(code = HttpStatus.OK)	    
	    @GetMapping("/{id}")	    
	    public T buscarPorId (@PathVariable long id){	 
	    	   return repositorio.findById(id)	    			   
		        		.orElseThrow ( () -> new EntidadeNaoEncontratException("Registro não encontrado Id:" + id));		    			   
	    		    	
	    }
	    @Override
	    @GetMapping 
    	public Iterable<T> listar () {
			//return repositorio.findAll();
			return repositorio.findByCriadoPor(this.usuario.get());
		}
	    @Override
	    @PostMapping
	    @Transactional
		@ResponseStatus(code = HttpStatus.CREATED) 
	    public T criar(@Validated @RequestBody T entidade){
	        return repositorio.save (entidade);
	    }
	    @Override
	    @PutMapping("")
	    @Transactional
	    @ResponseStatus(code = HttpStatus.OK)
	    public T atualizar(@RequestBody T entidade){
	        return repositorio.save(entidade);
	    }
	    @Override
	    @DeleteMapping("/{id}")
	    @Transactional
	    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	    public void excluirPorId(@PathVariable long id){
	    	buscarPorId(id);
	    	repositorio.deleteById(id);
	    }

	}
