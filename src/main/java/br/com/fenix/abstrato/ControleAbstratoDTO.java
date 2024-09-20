package br.com.fenix.abstrato;

import java.util.List;

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

import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import jakarta.validation.Valid;

public abstract class ControleAbstratoDTO<S extends ServicoAbstrato,T extends Persistable<ID>, 
                                          DTO extends Persistable<ID>, ID> implements IControleDTO<T,DTO ,ID>{
	
    	protected  S servico;

	    public ControleAbstratoDTO(S servico) {
 	        this.servico = servico;
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
		public String listarHtml() {
			return nomeEntidade().concat("/listar_").concat(nomeEntidade());	 
		}	

		@Override
		@GetMapping("/cadastrar")  	
		public ModelAndView cadastrar(DTO dto) {
			return new ModelAndView(cadastroHtml(),nomeEntidade(),dto);
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
		
		
	}
