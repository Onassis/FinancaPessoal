package br.com.fenix.abstrato.controle;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
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

import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import jakarta.validation.Valid;

public abstract   class ControleAbstratoDTO<S extends ServicoAbstrato,
										  T extends Persistable, 
                                          DTO extends Persistable, 
                                          ID> 										
										 implements IControleDTO<T,DTO,ID> {
	
	private final Class<T> entityClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	
	private final Class<T> dtoClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	
	protected S servico; 


  //


    public ControleAbstratoDTO(S servico) {
		this.servico = servico;
	//	this.converter = converter;					
		
	}
    public abstract  GenericConverter<T, DTO> getConverter(); 
    
	@Override
	public String nomeClasse() {
		return entityClass.getSimpleName().toLowerCase(); 
	}
	@Override
	public String nomeClasseDTO() {
		return dtoClass.getSimpleName().toLowerCase(); 
	}

	@Override
	public String cadastroHtml() {
		return nomeClasse().concat("/cad_").concat(nomeClasse());	    
	}
	
	
	@Override
	public String listarHtml() {
		System.out.println("nomeClasse: " + nomeClasse());
		return nomeClasse().concat("/listar_").concat(nomeClasse());	 
	}	

	@Override
	public String urlListar() {			
		String nomeEntidade = "/" + nomeClasse(); 
		return nomeEntidade.concat("/listar");	 
	}
	@Override
	public String  urlCadastrar() {			
		String nomeEntidade = "/" +  nomeClasse().concat("/cadastrar");
		return nomeEntidade;	 
	}
	@Override
	public String  urlEditar(ID id) {			
		String nomeEntidade = "/" +  nomeClasse().concat("/editar/").concat(id.toString());
		return nomeEntidade;	 
	}
	@Override
	public String cadastrar() {
		System.out.println("cadastrar " +  cadastroHtml());
		return  cadastroHtml() ;
	}

	@Override
	@GetMapping("/cadastrar")
	public String cadastrarDTO(DTO dto, ModelMap model) {
		model.addAttribute(nomeClasseDTO() , dto);
		return  cadastroHtml() ;
	}

	@GetMapping("/listar")
	@Override
	public String  listarView(ModelMap model) {
		System.out.println("listarView"); 
		System.out.println(servico);
	    Iterable<T> dados = servico.listar();
	    List<DTO> dtos = StreamSupport.stream(dados.spliterator(), false)
                    .map(dado -> getConverter().convertToDto(dado))
                    .collect(Collectors.toList());
		model.addAttribute(nomeClasseDTO(), dtos);
		return listarHtml();
	}

	@Override
	@GetMapping("/editar/{id}")
	public String atualizarView(ID id, ModelMap model, RedirectAttributes attr) {
		 if (model.containsAttribute("Erro")) {
			   return cadastroHtml();
		 }
		 try {
		   Optional<T>  entidadeOp = servico.buscarPorId(id);
		    DTO dto =  getConverter().convertToDto(entidadeOp.get()); 
		   
		   model.addAttribute(nomeClasseDTO() , dto);
		 } catch (RegistroNaoExisteException e) {
			 attr.addFlashAttribute("Erro", e.getMessage());   	
			 return "redirect:".concat(urlListar()); 
			 }		
	   return cadastroHtml();
	}
	@Override
	public String inserir(T entidade, RedirectAttributes attr) {
		try {	
			servico.criar(entidade);				
			attr.addFlashAttribute("Sucesso", "Registro inserido com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar Inserir -> Exception");			
			attr.addFlashAttribute("Erro", e.getMessage());
			attr.addFlashAttribute(nomeClasse(), entidade );
		}
		 			
		return "redirect:".concat(urlCadastrar());	
	}
	@Override
	public String alterar(T entidade, RedirectAttributes attr) {
		try {	
			servico.atualizar(entidade);		
			attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar -> Exception");
			attr.addFlashAttribute("Erro", e.getMessage());			
			attr.addFlashAttribute(nomeClasse() ,entidade);
			return "redirect:".concat(urlEditar((ID) entidade.getId()));		
			}
		 			
		return "redirect:".concat(urlListar());	
	}

	@Override
	@GetMapping("/excluir/{id}")   
	public String excluirPorId(@PathVariable ID id, RedirectAttributes attr) {
		try {
			servico.excluirPorId(id);
			attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");
		}	
		catch (Exception e) {
			attr.addFlashAttribute("Erro", e.getMessage()); 
		}		
		return  "redirect:".concat(urlListar());	
	}
	@PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	@Override
	public String salvarDTO(@Valid DTO dto, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return cadastrar() ;	    	
 		}
		if (dto.isNew()) { 
			T entidade = getConverter().convertToEntity(dto);
			return inserir(entidade,attr); 
		}
		
		Optional<T>  entidadeOp = servico.buscarPorId(dto.getId()); 
		T entidade = entidadeOp.get(); 
		entidade = getConverter().updateEntity(entidade,dto); 		
		return alterar (entidade,attr); 
	}
}
