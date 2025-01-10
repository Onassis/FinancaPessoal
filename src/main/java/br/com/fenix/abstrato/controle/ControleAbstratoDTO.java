package br.com.fenix.abstrato.controle;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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

import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoDTO;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import jakarta.validation.Valid;

public abstract class ControleAbstratoDTO<S extends ServicoDTO,
										  T extends Persistable, 
                                          DTO extends Persistable, 
                                          ID> 
										 extends ControleAbstrato
										 implements IControleDTO {
	
//		private final Class<T> entityClass = 
//			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
//	
//    	protected  S servico;
//
//    	@Autowired
//    	private DataSource dataSource;
    	
    
	public ControleAbstratoDTO(S servico) {
	       super(servico);
	     }

@Override 
public String atualizarView2( ID id, ModelMap model,RedirectAttributes attr) {
	return null;
}
	@Override
	public String cadastrarDTO(Persistable dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String salvarDTO(@Valid Persistable dto, BindingResult result, RedirectAttributes attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String atualizarView(Object id, ModelMap model, RedirectAttributes attr) {
		// TODO Auto-generated method stub
		return super.atualizarView(id, model, attr);
	}

	@Override
	public String alterar(Persistable entidade, RedirectAttributes attr) {
		// TODO Auto-generated method stub
		return super.alterar(entidade, attr);
	}


	    
////		@Override
////		@GetMapping("/cadastrar")  	
////		public String cadastrarDTO(DTO dto) {
////			return  cadastroHtml() ;
////		}
//		@Override
//		@GetMapping("/listar")  
//		public String  listarView(ModelMap model) {
//	//		model.addAttribute(nomeClasse(), servico.listarDto());
//			return listarHtml();
//		}
//		@Override
//		public String atualizarView(@PathVariable ID id, ModelMap model,RedirectAttributes attr) {
//			 if (model.containsAttribute("Erro")) {
//				   return cadastroHtml();
//			 }
//			 try {
//			   Optional<T>  entidadeOp = servico.buscarPorId(id);
//			   model.addAttribute(nomeClasse() , entidadeOp.get());
//		   } 
//		   catch (RegistroNaoExisteException e) {
//			   attr.addFlashAttribute("Erro", e.getMessage());   	
//			   return "redirect:".concat(urlListar());
//		   }		
//			   return cadastroHtml();
//			
//		}
//		@Override
//		@GetMapping("/editar/{id}")
//		public String atualizarView(ID id, ModelMap model,RedirectAttributes attr) {
//			 if (model.containsAttribute("Erro")) {
//				   return cadastroHtml();
//			 }
//			 try {
//			   Optional<T>  entidadeOp = servico.buscarPorId(id);
//			 //  DTO dto =  servico. EntidadeToDTO(entidadeOp.get());
//			 //  model.addAttribute(nomeClasse() , dto);
//		   } 
//		   catch (RegistroNaoExisteException e) {
//			   attr.addFlashAttribute("Erro", e.getMessage());   	
//			   return "redirect:".concat(urlListar());
//		   }		
//			   return cadastroHtml();
//			
//		}
//		


//		private String inserir(T entidade,RedirectAttributes attr) {
//			try {	
//				servico.criar(entidade);				
//				attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
//			}
//			catch (Exception e) {
//				System.out.println("ControleAbstrato-> Salvar Inserir -> Exception");			
//				attr.addFlashAttribute("Erro", e.getMessage());
//				attr.addFlashAttribute(nomeClasse(), entidade );
//			}
//			 			
//			return "redirect:".concat(urlCadastrar());	
//		}
//		private String alterar(T entidade,RedirectAttributes attr) {
//			try {	
//				servico.atualizar(entidade);		
//				attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
//			}
//			catch (Exception e) {
//				System.out.println("ControleAbstrato-> Salvar -> Exception");
//				attr.addFlashAttribute("Erro", e.getMessage());			
//				attr.addFlashAttribute(nomeClasse() ,entidade);
//				return "redirect:".concat(urlEditar(entidade.getId()));		
//				}
//			 			
//			return "redirect:".concat(urlListar());	
//		}
		

	
	
//		@Override
//		@GetMapping("/excluir/{id}")      
//		public String  excluirPorId(@PathVariable ID id, RedirectAttributes attr) {
//			try {
//				servico.excluirPorId(id);
//				attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");
//			}	
//			catch (Exception e) {
//				attr.addFlashAttribute("Erro", e.getMessage()); 
//			}		
//			return  "redirect:".concat(urlListar());													    	
//		}
//
//		@Override
//		public String urlListar() {			
//			String nomeEntidade = "/" + nomeEntidade(); 
//			return nomeEntidade.concat("/listar");	 
//		}
//		@Override
//		public String  urlCadastrar() {			
//			String nomeEntidade = "/" +  nomeEntidade().concat("/cadastrar");
//			System.out.println(nomeEntidade);
//			return nomeEntidade;	 
//		}
//		@Override
//		public String listarHtml() {
//			return nomeEntidade().concat("/listar_").concat(nomeEntidade());	 
//		}	
//
//		@Override
//		@GetMapping("/cadastrar")  	
//		public ModelAndView cadastrar(DTO dto) {
//			return new ModelAndView(cadastroHtml(),nomeEntidade(),dto);
//		}
//		@Override
//		@GetMapping("/listar")  
//		public ModelAndView listarView(ModelMap model) {
//			ModelAndView mv = new ModelAndView();
//
//			System.out.println("Listar");
//			String nomeEntidade = nomeEntidade();
//			mv.addObject(nomeEntidade, servico.listar());
//			mv.addObject(model);
//			mv.setViewName(listarHtml1());
//			return mv;
//		}
//		
//		
	}
