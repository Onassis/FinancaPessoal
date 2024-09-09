package br.com.fenix.abstrato;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
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
import org.springframework.web.servlet.view.RedirectView;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;
import jakarta.validation.Valid;

public abstract class ControleAbstrato<S extends ServicoAbstrato,
									   T extends Persistable<ID>,
									   ID> implements IControle<T ,ID>{
	

//    	@Value("${server.servlet.context-path}")
//    	private String contextPath;
	    
    	protected T entidade; 	    
        protected S servico; 


        public ControleAbstrato(S servico) {
			super();
			this.servico = servico;			
		}
		@Override
	 	public String nomeEntidade() {
	    	 return entidade.getClass().getSimpleName().toLowerCase(); 
	    }
	    @Override
		public String nomeCadastro() {
	    	return nomeEntidade().concat("/cad_").concat(nomeEntidade());	    
	    }
	    @Override
	    public String nomeListar() {
	    	return nomeEntidade().concat("/listar_").concat(nomeEntidade());	 
	    }	
	    
	    @Override
		public String urlListar() {			
	    	String nomeEntidade = "/" + nomeEntidade(); 
	    	return nomeEntidade.concat("/listar_").concat(nomeEntidade());	 
		}
	    @Override
		public String  urlCadastrar() {			
	    	String nomeEntidade = "/" + entidade.getClass().getSimpleName().toLowerCase().concat("/cadastrar");
	    	System.out.println(nomeEntidade);
	    	return nomeEntidade;	 
		}
	    
		@Override
	    @GetMapping("/cadastrar")  	
	    public String cadastrar(T entidade) {
	       	return nomeCadastro();
	    }
		@Override
		@GetMapping("/listar")  
		public String listarView(ModelMap model) {
			System.out.println("Listar");
			String nomeEntidade = nomeEntidade();
			model.addAttribute(nomeEntidade, servico.listar());
			return urlListar();
//			return "conta/listar_conta";
		}
		
		
	    @Override
	    @GetMapping("/editar/{id}")  
		public String atualizarView(@PathVariable ID id,ModelMap model) {
	    	 try {
		    	Optional<T>  entidadeOp = servico.buscarPorId(id);   		
		    	model.addAttribute(nomeEntidade(),entidadeOp.get());
		    	System.out.println("atualiza");
				return nomeCadastro() ;		  			  	    		
	    	} catch (RegistroNaoExisteException e) {
	    		model.addAttribute("Erro", e.getMessage());          	
		        return listarView(model);
	    	}
		}  	  
	  	
	    @Override
	    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	    @Transactional 
	    public String salvar(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
	    	if (result.hasErrors()) {
	    		System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar());
	    		return  nomeCadastro();
			}
	        try {
	        	
	            if (entidade.isNew()) {
	    	      	System.out.println("ControleAbstrato-> Criar");

			        servico.criar(entidade);
			    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");		
	            }  
	            else {
	    	      	System.out.println("ControleAbstrato-> Editar");
	            	servico.atualizar(entidade);
			    	attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");
		    		return "redirect:" + urlListar();
	            }	            	            	
	        } catch (Exception e) {
	        	System.out.println("ControleAbstrato-> Criar -> Error");
		    	attr.addFlashAttribute("Erro", e.getMessage());        	
		    	attr.addFlashAttribute(nomeEntidade(), entidade);
		   	 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        }
	       
	    	return nomeCadastro();				
			
	    	
	    }
	    
	    
	    @Override
	    @GetMapping("/excluir/{id}")      
	    public String excluirPorId(@PathVariable ID id , ModelMap model) { 
	      	System.out.println("ControleAbstrato-> Excluir ");
	  

	       	try {
	        	servico.excluirPorId(id);
	        	model.addAttribute("Sucesso", "Registro excluido com sucesso.");
	        }	        
	    	catch (NegocioException e) {
	        	System.out.println("ControleAbstrato-> Excluir -> Error");
	        	System.out.println(e.getMessage());
	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        	model.addAttribute("Erro", e.getMessage());     
	        }
	       	finally {
		      	System.out.println("ControleAbstrato-> Excluir  -> Redirect Conta/listar");
		    	return listarView(model) ;						
				
			}	    	
	    }
//		@Override
//		public ModelAndView listarView() {
//			System.out.println("Listar");
//			Iterable<T> dados = servico.listar();
//			return new ModelAndView(nomeListar(),nomeEntidade(),dados) ;	    	
//		}
	    
//	    @Override
//	    @Transactional 
//	    public RedirectView salvar2(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
//	    	if (result.hasErrors()) {
//	    		System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar());
//	    	   	attr.addFlashAttribute(nomeEntidade(), entidade);
//	    		return new RedirectView( urlCadastrar(),true) ;
//			}
//	        try {
//	        	
//	            if (entidade.isNew()) {
//	    	      	System.out.println("ControleAbstrato-> Criar");
//			        servico.criar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");		
//	            }  
//	            else {
//	    	      	System.out.println("ControleAbstrato-> Editar");
//	            	servico.atualizar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");			   
//		    		return new RedirectView(urlListar(),true) ;			  
//	            }
//	                        	
//	        } 
//	        
//	        catch (NegocioException e) {
//	        	System.out.println("ControleAbstrato-> Criar -> Error");
//	        	attr.addFlashAttribute("Erro", e.toString());       
//	           	attr.addFlashAttribute(nomeEntidade(), entidade);
//	        	 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//    		return new RedirectView( urlCadastrar(),true) ;
//	    	
//	    }
	    
//	    @GetMapping("/deletar/{id}")  
//	    @Transactional	   
//	    public  ModelAndView excluirPorId2(@PathVariable ID id) {
//	       	try {
//	        	servico.excluirPorId(id);
//		    	attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");			        		        		    	
//	        }	        
//	    	catch (NegocioException e) {
//	        	System.out.println("ControleAbstrato-> Excluir -> Error");
//	        	System.out.println(e.getMessage());
//	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	    
//	        	attr.addFlashAttribute("Sucesso", e.getMessage());        
//	        } 
//	       	finally {
//	          	System.out.println("ControleAbstrato-> Excluir2  -> Redirect Conta/listar");
////		    	return new RedirectView("/" + nomeClasse + "/listar"	,true) ;						
//		    	return listarView2() ;			       		
//	       	}
//		
//	    }
	  
	    
	    
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

	}
