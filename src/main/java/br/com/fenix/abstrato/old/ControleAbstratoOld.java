package br.com.fenix.abstrato.old;

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

public abstract class ControleAbstratoOld{ 
	
}
//public abstract class ControleAbstratoOld<S extends ServicoAbstrato,
//									   T extends Persistable<ID>,
//									   ID> implements IControleOld<T ,ID>{
//	
//
//    	@Value("${server.servlet.context-path}")
//    	private String contextPath;
//	    
//        protected S servico; 
//        // Método para retornar o nome da classe T
//        public String getNomeClasseT() {
//            // Supondo que você tenha uma instância de T
//            T instancia = getInstanciaT();
//            return instancia.getClass().getSimpleName();
//        }
//
//        // Método abstrato que deve ser implementado pelas subclasses para fornecer uma instância de T
//        protected abstract T getInstanciaT();
//
//        public ControleAbstratoOld(S servico) {
//			super();
//			this.servico = servico;
//		}
//        public String nomeClasse(Class<?> classe) {
//        	return classe.getName().toLowerCase();
//        }
//		@Override
//	 	public String nomeEntidade(T entidade) {
//	    	 return entidade.getClass().getSimpleName().toLowerCase(); 
//	    }
//	    @Override
//		public String nomeCadastro(T entidade) {
//	    	String nomeEntidade = nomeEntidade(entidade); 
//
//	    	return nomeEntidade.concat("/cad_").concat(nomeEntidade);	    
//	    }
//	    @Override
//	    public String nomeListar(T entidade) {
//	    	String nomeEntidade = entidade.getClass().getSimpleName().toLowerCase(); 
//	    	return nomeEntidade.concat("/listar_").concat(nomeEntidade);	 
//	    }	
//	    
//	    @Override
//		public String urlListar(T entidade) {			
//	    	String nomeEntidade = "/" + entidade.getClass().getSimpleName().toLowerCase(); 
//	    	return nomeEntidade.concat("/listar");	 
//		}
//	    @Override
//		public String  urlCadastrar(T entidade) {			
//	    	String nomeEntidade = "/" + entidade.getClass().getSimpleName().toLowerCase().concat("/cadastrar");
//	    	System.out.println(nomeEntidade);
//	    	return nomeEntidade;	 
//		}
//	    
//		@Override
//	    @GetMapping("/cadastrar")  	
//	    public ModelAndView cadastrar(T entidade) {
//	       	return new ModelAndView(nomeCadastro(entidade),nomeEntidade(entidade),entidade);
//	    }
//		@Override
//		@GetMapping("/listar")  
//		public ModelAndView listarView(T entidade) {
//			System.out.println("Listar");
//			Iterable<T> dados = servico.listar();
//			return new ModelAndView(nomeListar(entidade),nomeEntidade(entidade),dados) ;	    	
//		}
//	    @Override
//	    @GetMapping("/editar/{id}")  
//		public ModelAndView atualizarView(@PathVariable ID id) {    	
//	    	Optional<T>  entidadeOp = servico.buscarPorId(id);   	
//	    	T entidade = entidadeOp.get();
//	    	System.out.println("atualiza");
//			return new ModelAndView(nomeCadastro(entidade),nomeEntidade(entidade),entidade) ;		  			  
//		}  	  
//	  	
//	    @Override
//	    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//	    @Transactional 
//	    public ModelAndView salvar(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
//	    	if (result.hasErrors()) {
//	    		System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar(entidade));
//	    		return new ModelAndView(urlCadastrar(entidade),nomeEntidade(entidade),entidade);
//			}
//	        try {
//	        	
//	            if (entidade.isNew()) {
//	    	      	System.out.println("ControleAbstrato-> Criar");
//
//			        servico.criar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");		
//	            }  
//	            else {
//	    	      	System.out.println("ControleAbstrato-> Editar");
//	            	servico.atualizar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");
//		    		return new ModelAndView("redirect:" + urlListar(entidade),nomeEntidade(entidade),entidade);
//	            }	            	            	
//	        } catch (Exception e) {
//	        	System.out.println("ControleAbstrato-> Criar -> Error");
//		    	attr.addFlashAttribute("Erro", e.getMessage());        	
//		    	attr.addFlashAttribute(nomeEntidade(entidade), entidade);
//		   	 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//	        }
//	       
//	    	return new ModelAndView("redirect:" + urlCadastrar(entidade));				
//			
//	    	
//	    }
//	    
//	    @Override
//	    @Transactional 
//	    public RedirectView salvar2(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
//	    	if (result.hasErrors()) {
//	    		System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar(entidade));
//	    	   	attr.addFlashAttribute(nomeEntidade(entidade), entidade);
//	    		return new RedirectView( urlCadastrar(entidade),true) ;
//			}
//	        try {
//	        	
//	            if (entidade.isNew()) {
//	    	      	System.out.println("ControleAbstrato-> Criar");
//			        entidade = (@Valid T) servico.criar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");		
//	            }  
//	            else {
//	    	      	System.out.println("ControleAbstrato-> Editar");
//	            	servico.atualizar(entidade);
//			    	attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");			   
//		    		return new RedirectView(urlListar(entidade),true) ;			  
//	            }
//	                        	
//	        } 
//	        
//	        catch (NegocioException e) {
//	        	System.out.println("ControleAbstrato-> Criar -> Error");
//	        	attr.addFlashAttribute("Erro", e.toString());       
//	           	attr.addFlashAttribute(nomeEntidade(entidade), entidade);
//	        	 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//    		return new RedirectView( urlCadastrar(entidade),true) ;
//	    	
//	    }
//	    
//	    @Override
//	    @GetMapping("/deletar/{id}")  
//	    @Transactional
//	    @ResponseStatus(code = HttpStatus.NO_CONTENT)	    
//	    public ModelAndView excluirPorId(@PathVariable ID id , RedirectAttributes attr) { 
//	    	String nomeClasse = getNomeClasseT();
//	       	try {
//	        	servico.excluirPorId(id);
//		    	attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");			        		        		    	
//	        }	        
//	    	catch (NegocioException e) {
//	        	System.out.println("ControleAbstrato-> Excluir -> Error");
//	        	System.out.println(e.getMessage());
//	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	    
//	        	attr.addFlashAttribute("Erro", e.getMessage());        
//	        }
//	        System.out.println(nomeClasse);
//	    	return new ModelAndView("redirect:/conta/listar") ;						
//	    	
//	    }
////	    public  RedirectView excluirPorId(@PathVariable ID id) {
////	    	String nomeClasse = getNomeClasseT();
////	       	try {
////	        	servico.excluirPorId(id);
////		    	attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");			        		        		    	
////	        }	        
////	    	catch (NegocioException e) {
////	        	System.out.println("ControleAbstrato-> Excluir -> Error");
////	        	System.out.println(e.getMessage());
////	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	    
////	        	attr.addFlashAttribute("Sucesso", e.getMessage());        
////	        }
////	        System.out.println(nomeClasse);
////	    	return new RedirectView("/" + nomeClasse + "/listar"	,true) ;						
////	    }
//	  
//	    
//	    
////	    @GetMapping("/")
////	    public String findAllPage(
////	            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
////	            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
////	            Model model,
////	            @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
////	        Page<Student> studentPage = studentRepo.findByNameContains(keyWord,PageRequest.of(pageNumber, size));
////	        model.addAttribute("students", studentPage.getContent());
////	        model.addAttribute("pages", new int[studentPage.getTotalPages()]);
////	        model.addAttribute("currentPage", pageNumber);
////	        model.addAttribute("keyWord", keyWord);
////	        return "index";
////	    }
//
//	}
