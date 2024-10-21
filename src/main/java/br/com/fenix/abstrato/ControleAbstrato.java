package br.com.fenix.abstrato;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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

	private final Class<T> entityClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	
	protected S servico; 
	@Autowired
	private DataSource dataSource;


	public ControleAbstrato(S servico) {
		super();
		this.servico = servico;			

	}
	public String nomeClasse() {
		return entityClass.getSimpleName().toLowerCase(); 
	}
	@Override
	public String nomeEntidade() {
		return novaInstacia().getClass().getSimpleName().toLowerCase(); 
	}
	@Override
	public String cadastroHtml() {
		return nomeClasse().concat("/cad_").concat(nomeEntidade());	    
	}
	
	
	@Override
	public String listarHtml() {
		return nomeClasse().concat("/listar_").concat(nomeEntidade());	 
	}	

	@Override
	public String urlListar() {			
		String nomeEntidade = "/" + nomeClasse(); 
		return nomeEntidade.concat("/listar");	 
	}
	@Override
	public String  urlCadastrar() {			
		String nomeEntidade = "/" +  nomeClasse().concat("/cadastrar");
		System.out.println(nomeEntidade);
		return nomeEntidade;	 
	}
	@Override
	public String  urlEditar(ID id) {			
		String nomeEntidade = "/" +  nomeClasse().concat("/editar/").concat(id.toString());
		System.out.println(nomeEntidade);
		return nomeEntidade;	 
	}

	 
	@Override
	@GetMapping("/cadastrar")  	
	public String cadastrar(T entidade) {
		return  cadastroHtml() ;
	}
	public String redirectCadastrar(T entidade) {
		return  "redirect:".concat(cadastroHtml()) ;
	}
	@Override
	@GetMapping("/listar")  
	public String  listarView(ModelMap model) {
		System.out.println("nome da classe: " + nomeClasse());
		String nomeEntidade = nomeEntidade();
		model.addAttribute(nomeEntidade, servico.listar());
		return listarHtml();
	}
	@GetMapping("/editar/{id}")	    
	public String atualizarView(@PathVariable ID id, ModelMap model, RedirectAttributes attr) {
        String pagina = cadastroHtml();

       if (attr.getAttribute(nomeClasse()) != null) {
    	   model.addAttribute(nomeEntidade(), attr.getAttribute(nomeClasse()));
       }
       else {  
    	   try {
    		   Optional<T>  entidadeOp = servico.buscarPorId(id);
    		   model.addAttribute(nomeEntidade(), entidadeOp.get());		
    	   } 
    	   catch (RegistroNaoExisteException e) {
    		   attr.addFlashAttribute("Erro", e.getMessage());          	
    		   pagina = "redirect:".concat(urlListar());
    	   }		
       }
       return pagina;				  			  	    		
	}
	private String inserir(T entidade,RedirectAttributes attr) {
		try {	
			servico.criar(entidade);				
			attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar -> Exception");
			attr.addFlashAttribute("Erro", e.getMessage());
			return redirectCadastrar(entidade);	
			}
		 			
		return "redirect:".concat(urlCadastrar());	
	}
	private String alterar(T entidade,RedirectAttributes attr) {
		try {	
			servico.atualizar(entidade);		
			attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar -> Exception");
			attr.addFlashAttribute("Erro", e.getMessage());
			attr.addFlashAttribute(nomeClasse() ,entidade);
			return cadastrar(entidade);	
			}
		 			
		return "redirect:".concat(urlListar());	
	}
	
	@Override
	@PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String salvar(@Valid @ModelAttribute  T entidade,BindingResult result, RedirectAttributes attr) {
		ModelMap model = new ModelMap();
		if (result.hasErrors()) {
			return cadastrar(entidade) ;	    	
		}
		
		if (entidade.isNew()) { 
			return inserir(entidade,attr); 
		}
		return alterar (entidade,attr); 
	}
//		
//			System.out.println("ControleAbstrato-> Criar");
//			try {	
//				servico.criar(entidade);				
//				attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
//				catch (Exception e) {
//					System.out.println("ControleAbstrato-> Salvar -> Exception");
//					attr.addFlashAttribute("Erro", e.getMessage());
//				}
//			} 			
//			return "redirect:".concat(urlCadastrar());				
//		}  
//		else protected abstract String alterar(@Valid T entidade, BindingResult result, RedirectAttributes attr);
//		{
//			System.out.println("ControleAbstrato-> Editar");
//			servico.atualizar(entidade);
//			attr.addFlashAttribute("Sucesso", "Registro alterado com sucesso.");
//			return "redirect:".concat(urlListar());	
//			}	            	            	
//		}	 
//		catch (protected abstract String criar(@Valid T entidade, BindingResult result, RedirectAttributes attr);
//		NegocioException e) {
//			System.out.println("ControleAbstrato-> Salvar -> NegocioException");
//			attr.addFlashAttribute(nomeEntidade(), entidade);
//			
//			attr.addFlashAttribute("Erro", e.getMessage());
//		//	model.addAttribute("Erro", e.getMessage());	
//			model.addAttribute(nomeEntidade(), entidade); 
//			return "redirect:conta/editar/502"; 
//		}
//		catch (Exception e) {
//			System.out.println("ControleAbstrato-> Salvar -> Exception");
//			attr.addFlashAttribute("Erro", e.getMessage());
//			return atualizarView(entidade.getId(), model, attr);
//		}

	
 	  

	@Override
	@GetMapping("/excluir/{id}")      
	public String  excluirPorId(@PathVariable ID id, RedirectAttributes attr) {
		try {
			servico.excluirPorId(id);
			attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");
		}	
		catch (Exception e) {
			attr.addFlashAttribute("Erro", e.getMessage()); 
		}		
		return  "redirect:".concat(urlListar());													    	
	}

}

