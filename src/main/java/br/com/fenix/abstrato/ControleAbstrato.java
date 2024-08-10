package br.com.fenix.abstrato;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import jakarta.validation.Valid;

public abstract class ControleAbstrato<T,ID> implements IControle<T,ID>{
	   
        ServicoAbstrato<T, ID> servico; 
        
        
	    public ControleAbstrato(ServicoAbstrato<T, ID> servico) {
			super();
			this.servico = servico;
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
		public ModelAndView atualizarView(@PathVariable ID id) {    	
			T entidade = buscarPorId(id); 
			return new ModelAndView(nomeCadastro(entidade),nomeEntidade(entidade),entidade) ;		  			  
		}  	  
	    @Override
		@GetMapping("/listar")  
		public ModelAndView listarView(T entidade) {
	    	System.out.println("Listar");
			Iterable<T> dados = servico.listar();
			return new ModelAndView(nomeListar(entidade),nomeEntidade(entidade),dados) ;
	    	
		}	
	    
//	    @GetMapping("/")
//	    public String findAllPage(
//	            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
//	            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
//	            Model model,
//	            @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
//	        Page<Student> studentPage = studentRepo.findByNameContains(keyWord,PageRequest.of(pageNumber, size));
//	        model.addAttribute("students", studentPage.getContent());
//	        model.addAttribute("pages", new int[studentPage.getTotalPages()]);
//	        model.addAttribute("currentPage", pageNumber);
//	        model.addAttribute("keyWord", keyWord);
//	        return "index";
//	    }
	    @Override
	    @ResponseStatus(code = HttpStatus.OK)	    
	    @GetMapping("/{id}")	    
	    public T buscarPorId (@PathVariable ID id) throws RegistroNaoExisteException{	 
//	    	   return servico.buscarPorId(id);
	    	return null;
	    }
	    @Override
	    @GetMapping 
    	public Iterable<T> listar () {
//			return servico.listar();	  
			return null;
		}
	    @Override
	    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	    @Transactional 
	    public ModelAndView criar(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
	      	System.out.println("ControleAbstrato-> Criar");
	    	if (result.hasErrors()) {
				return cadastrar(entidade);
			}
	        try {
		    	entidade = servico.criar(entidade); 
		    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
	        } catch (Exception e) {
	        	System.out.println("ControleAbstrato-> Criar -> Error");
		    	attr.addFlashAttribute("Erro", e.toString());        	
//	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }	
	    	return cadastrar(entidade);//	            return new ResponseEntity<>(bookObj, HttpStatus.CREATED);
	    	
	    }
	    

	    @Override
	    @PutMapping("")
	    @Transactional
	    @ResponseStatus(code = HttpStatus.OK)
	    public T atualizar(@Valid @ModelAttribute  T entidade){
//	        return servico.atualizar(entidade);
	        return null;
	    }
	    @Override
	    @DeleteMapping("/{id}")
	    @Transactional
	    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	    public void excluirPorId(@PathVariable ID id){
	    	buscarPorId(id);
	    //	servico.excluirPorId(id);
	    }
	    @Override
	    @DeleteMapping("/all")
	    @Transactional
	    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	    public void excluirTodos(){
	    	//servico.excluirTodos();
	    }

	}
