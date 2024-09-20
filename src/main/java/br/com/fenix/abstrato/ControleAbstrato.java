package br.com.fenix.abstrato;

import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Persistable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.util.DbUtil;
import jakarta.validation.Valid;

import org.postgresql.util.*;

public abstract class ControleAbstrato<S extends ServicoAbstrato,T extends Persistable<ID>,ID> implements IControle<T ,ID>{


	//    	@Value("${server.servlet.context-path}")
	//    	private String contextPath;

	//    	protected T entidade;   
	protected S servico; 
	@Autowired
	private DataSource dataSource;


	public ControleAbstrato(S servico) {
		super();
		this.servico = servico;			

	}
	@Override
	public String nomeEntidade() {
		return novaInstacia().getClass().getSimpleName().toLowerCase(); 
	}
	@Override
	public String cadastroHtml() {
		return nomeEntidade().concat("/cad_").concat(nomeEntidade());	    
	}
	@Override
	public String listarHtml() {
		return nomeEntidade().concat("/listar_").concat(nomeEntidade());	 
	}	

	@Override
	public String urlListar() {			
		String nomeEntidade = "/" + nomeEntidade(); 
		return nomeEntidade.concat("/listar");	 
	}
	@Override
	public String  urlCadastrar() {			
		String nomeEntidade = "/" +  nomeEntidade().concat("/cadastrar");
		System.out.println(nomeEntidade);
		return nomeEntidade;	 
	}

	@Override
	@GetMapping("/cadastrar")  	
	public ModelAndView cadastrar(T entidade) {
		return new ModelAndView(cadastroHtml(),nomeEntidade(),entidade);
	}
	@Override
	@GetMapping("/listar")  
	public ModelAndView listarView(ModelMap model) {
		ModelAndView mv = new ModelAndView();

		System.out.println("Listar");
		String nomeEntidade = nomeEntidade();
		mv.addObject(nomeEntidade, servico.listar());
		mv.addObject(model);
		mv.setViewName(listarHtml());
		return mv;
	}

	@Override
	@PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ModelAndView salvar2(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
		ModelAndView mv = new ModelAndView();
		if (result.hasErrors()) {
			System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar());
			mv.addObject(nomeEntidade(), entidade);
			mv.setViewName(cadastroHtml());
			return mv;	    	
		}
		try {

			if (entidade.isNew()) {
				System.out.println("ControleAbstrato-> Criar");
				servico.criar(entidade);
				//			    	attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
				mv.addObject(nomeEntidade(), novaInstacia());
				mv.addObject("Sucesso", "Registro inserido com sucesso.");
				mv.setViewName(cadastroHtml());
//				return mv;	    	

			}  
			else {
				System.out.println("ControleAbstrato-> Editar");
				servico.atualizar(entidade);
				attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");

				RedirectView rv = new RedirectView(urlListar(), true, true, false);

				mv.addObject(rv);
				//	mv.setViewName(nomeListarHtml());

//				return new ModelAndView(rv);

			}	            	            	
		}	 
		catch (NegocioException e) {
			System.out.println("ControleAbstrato-> Excluir -> NegocioException");
			mv.setViewName(cadastroHtml());	  
			attr.addFlashAttribute(nomeEntidade(), entidade);
			mv.addObject("Erro", e.getMessage());
			System.out.println(e.getMessage());
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Criar -> Error");	      	 		
			mv.addObject("Erro",DbUtil.handleSQLException(e)); 
			mv.setViewName(cadastroHtml());	        	
			mv.addObject("Erro", e.getMessage());
			attr.addFlashAttribute(nomeEntidade(), entidade);
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		return mv;			
	}



	@GetMapping("/editar/{id}")	    
	public ModelAndView atualizarView(@PathVariable ID id) {
		ModelAndView mv = new ModelAndView();
		System.out.println("ControleAbstrato-> atualizarView ");

		try {
			Optional<T>  entidadeOp = servico.buscarPorId(id);   		
			mv.addObject(nomeEntidade(), entidadeOp.get());
			mv.setViewName(cadastroHtml());
		} catch (RegistroNaoExisteException e) {
			System.out.println("ControleAbstrato-> atualizarView -> Error");
			mv.addObject("Erro", e.getMessage());          	
			mv.setViewName(listarHtml());
		}
		return mv;				  			  	    		

	}  	  
	@Override
	@GetMapping("/excluir/{id}")      
	public ModelAndView  excluirPorId(@PathVariable ID id, ModelMap model) {
		System.out.println("ControleAbstrato-> Excluir ");	    	
//		ModelAndView mv = new ModelAndView();

		try {
			servico.excluirPorId(id);
			model.addAttribute("Sucesso", "Registro excluido com sucesso.");
		}	
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Criar -> Error");	      	 		
//			mv.addObject("Erro",DbUtil.handleSQLException(e)); 
//			mv.setViewName(cadastroHtml());	        	
			model.addAttribute("Erro", e.getMessage());
 
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
//		catch (NegocioException e) {
//			System.out.println("ControleAbstrato-> Excluir -> NegocioException");
//			System.out.println(e.getMessage());
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			model.addAttribute("Erro", e.getMessage());
//		}
		finally {
			System.out.println("ControleAbstrato-> Excluir  -> Redirect Conta/listar");

			System.out.println(urlListar());
			return  new ModelAndView("forward:" + urlListar(),model);										
		}	    	
	}
	//	    @Override
	//	    @GetMapping("/excluir/{id}")      
	//	    public ModelAndView  excluirPorId(@PathVariable ID id ) {
	//	    	System.out.println("ControleAbstrato-> Excluir ");	
	//	    	System.out.println(urlListar());
	//	    	ModelAndView mv = new ModelAndView();
	//	    	RedirectView rv = new RedirectView(urlListar(), false, true, false);
	//	       	try {
	//	        	servico.excluirPorId(id);
	////	        	model.addAttribute("Sucesso", "Registro excluido com sucesso.");
	//	        	mv.addObject("Sucesso", "Registro excluido com sucesso.");
	//	        }	        
	//	       	catch (DataAccessException e) {
	//	        	System.out.println("ControleAbstrato-> Excluir -> DataAccessException");
	//	       		
	//	        	System.out.println(e.getMessage());
	//	         	mv.addObject("Erro", e.getMessage()); 
	//	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	//////	       		ServerErrorMessage error = ((PSQLException) e.getCause()).getServerErrorMessage();
	////	       		ServerErrorMessage error;
	////	       		//= e.getCause();
	////	       		System.out.println(error.getTable());
	////	       		System.out.println(error.getColumn());
	////	       		System.out.println(error.getConstraint());
	//	       		}
	//	    	catch (NegocioException e) {
	//	        	System.out.println("ControleAbstrato-> Excluir -> Error");
	//	        	System.out.println(e.getMessage());
	//	        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	//	//        	model.addAttribute("Erro", e.getMessage()); 
	//	         	mv.addObject("Erro", e.getMessage()); 
	//	        	
	//	        }
	//	       	finally {
	//		      	System.out.println("ControleAbstrato-> Excluir  -> Redirect conta/listar");
	//		    return mv;										
	//			}	    	
	//	    }
	//	  	
	//	    @Override
	//	    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	//	    @Transactional 
	//	    public String salvar(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) { 	
	//	    	if (result.hasErrors()) {
	//	    		System.out.println("Controle Abstrato -> Salvar -> error " + urlCadastrar());
	//	    		return  nomeCadastro();
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
	//		    		return "redirect:" + urlListar();
	//	            }	            	            	
	//	        } catch (Exception e) {
	//	        	System.out.println("ControleAbstrato-> Criar -> Error");
	//		    	attr.addFlashAttribute("Erro", e.getMessage());        	
	//		    	attr.addFlashAttribute(nomeEntidade(), entidade);
	//		   	 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	//	        }
	//	       
	//	    	return nomeCadastro();				
	//			
	//	    	
	//	    }
	//	    

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
////	    }
//
//catch (	DataIntegrityViolationException e) {
//	System.out.println("ControleAbstrato-> Excluir -> DataIntegrityViolationException");
//	System.out.println(e.getMessage());
//	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//}
//catch (DataAccessException e) {
//	System.out.println("ControleAbstrato-> Excluir -> DataAccessException");
//	System.out.println(e.getLocalizedMessage());
//
//	System.out.println("ControleAbstrato-> Excluir -> DataAccessException");
//	Throwable rootCause = e.getRootCause();
//
//	Throwable t = rootCause;
//	while(t != null) {
//		System.out.println("Cause: " + t);
//		t = t.getCause();
//	}
//
//	//	    	            if (rootCause instanceof PSQLException) {
//	//	     	      		ServerErrorMessage error = (PSQLException) e.getServerErrorMessage();
//	//	     	      		System.out.println("Sql Tabela --->" + error.getTable());
//	//	     	      		System.out.println(error.getColumn());
//	//	     	      		System.out.println(error.getConstraint());
//	//	     	      		System.out.println("Sql Schema --->" + error.getSchema());
//	//	    	            }
//
//	System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
//	if (rootCause instanceof SQLException) {
//
//		SQLException sqlEx = (SQLException) rootCause;
//		DbUtil.handleSQLException(sqlEx); 
//		//	                SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
//		//	                DataAccessException translatedEx = translator.translate("Operação de banco de dados", null, sqlEx);
//		//	                System.err.println("Sentença SQL que causou o erro: " + translatedEx.getMessage());
//		//	                System.err.println("Sql State --->" + sqlEx.getSQLState());
//	}
//	System.out.println("Mensagem ->" + rootCause.getMessage());
//	model.addAttribute("Erro", e.getMessage());
//	System.out.println(e.getMessage());
//	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//}