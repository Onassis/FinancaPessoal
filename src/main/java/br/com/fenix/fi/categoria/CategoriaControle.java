package br.com.fenix.fi.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fenix.abstrato.controle.ControleAbstrato;
import br.com.fenix.abstrato.controle.ControleAbstratoDTO;
import br.com.fenix.abstrato.controle.IControle;
import br.com.fenix.abstrato.controle.IControleDTO;
import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoServico;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.fi.subCategoria.SubCategoriaServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaControle extends 	ControleAbstratoDTO<Categoria,CategoriaDTO,Long> 
			implements IControleDTO<Categoria,CategoriaDTO,Long>   {

	@Autowired
	ContaServico contaSC;
	@Autowired
	CategoriaServico servico;
	@Autowired
	SubCategoriaServico sSubCategoria;
	
	//private GenericConverter<Categoria, CategoriaDTO> converter;

	public CategoriaControle() {	
	}
	@Override
	public ServicoAbstratoDTO getServico() {
	
		return servico;
	}
	@ModelAttribute("opTiposLancamento")	
    public List<Option> getOptionsFromTipoLancamento() {
		return TipoLancamento.listaTipoLancamento();
    }

	@ModelAttribute("opTipoCategoria")	
    public List<Option> getOptionsFromTipoCategoria() {
        return TipoCategoria.listaTipoCategoria();
    }
	@ModelAttribute("opContas")
	@Cacheable(value="conta", sync = true)
	public List<Option>  listaDeContas() {
		   return contaSC.listaDeContas(TipoConta.CC); 		
	}
	
   @ModelAttribute("categoriasDTO")
   @Cacheable(value="categoria", sync = true)
	public List<Option> listaDeCategorias() {	   
	   return servico.listaDeCategoriasOpt(TipoCategoria.RE); 
	}

   @GetMapping("/listar/{tipoCategoria}")  
	public ModelAndView listarView(@PathVariable TipoCategoria tipoCategoria) {	
		System.out.println( tipoCategoria);
		List<CategoriaDTO>  dados = servico.listaPorTipoCategorias(tipoCategoria)	;	
		return new ModelAndView("categoria/listar_categoria","categoriaDTO", dados) ;		  			  
	}
	


/*----------------------- Lista os dados da tabela NAV TAB ------------------ */	
	@GetMapping("/lista_tab/{tipo}")  
	public ModelAndView listar_TabView(@PathVariable TipoCategoria tipo) {	
		System.out.println( tipo);
   	List<CategoriaDTO>  dados = servico.listaPorTipoCategorias(tipo)	;
		System.out.println( dados.size());
   	
		return new ModelAndView("categoria/lista_tab","categoriaDTO", dados) ;		  			  
	}

//---------------------------- SubCategoria ---------------------------------------------------------	
   @GetMapping("{id}/subcategoria/{id2}") 
   @ResponseStatus(code = HttpStatus.OK)	    	    
   public CategoriaDTO buscarSubCategoria (@PathVariable  long id2){
          return sSubCategoria.buscaDTOPorId(id2);
   }

   @GetMapping("{id}/subcategoria/editar/{id2}") 
   @ResponseStatus(code = HttpStatus.OK)	
   public ModelAndView editar_sub_item(@PathVariable long id, @PathVariable long id2) {
   		System.out.println("editar subcategoria");
   	   CategoriaDTO subCategoria = sSubCategoria.buscaDTOPorId(id2);  		
	   return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
   }    

   @GetMapping("{id}/subcategoria/cadastrar") 
   @ResponseStatus(code = HttpStatus.OK)	
   public ModelAndView cadastrar_sub_item(@PathVariable long id) {    	
   		System.out.println("Cadastro subcategoria");   		
   		Categoria categoria = servico.buscarPorId(id).get(); 
   		CategoriaDTO subCategoria = new CategoriaDTO(categoria);  	
		return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
   }    
   

   @PostMapping("/{id}/subcategoria")
   @ResponseStatus(code = HttpStatus.CREATED) 
   public String criarSubCategoria(@PathVariable long id, @Validated  CategoriaDTO subCategoria,BindingResult result, RedirectAttributes attr) throws Exception{

		if (result.hasErrors()) {
			attr.addFlashAttribute("subcategoria", subCategoria);
			return  "redirect:/categoria/" + id + "/subcategoria"; 
 		}
		
		try {	
			if (subCategoria.isNew()) { 
				subCategoria = sSubCategoria.criarDTO(subCategoria); 				
			}
			else { 
				subCategoria = sSubCategoria.atualizarDTO(subCategoria);
			}
			attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar -> Exception");
			attr.addFlashAttribute("Erro", e.getMessage());			
			 return  "redirect:/categoria/listar/RE"  ;
		}
		 			
   		return    "redirect:/categoria/listar/RE"  ;	   	
   }
  
//   @PostMapping("/{id}/subcategoria/{idSub}")
//   @Transactional
//   @ResponseStatus(code = HttpStatus.OK)
//   public  String  atualizar(@Validated CategoriaDTO subCategoria,BindingResult result, RedirectAttributes attr) {
//     try {
//	    subCategoria = sSubCategoria.atualizarDTO(subCategoria);
//	    attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
//     }
//     catch (Exception e) {
//		System.out.println("ControleAbstrato-> Salvar -> Exception");
//		attr.addFlashAttribute("Erro", e.getMessage());			
//		return   "redirect:/categoria/listar/RE" ;		
//     }
//	 			
//	 return  "redirect:/categoria/listar/RE" ;	   	
//   }
//   
//   @DeleteMapping("/{id}/subcategoria/{id2}")
//   @Transactional
//   @ResponseStatus(code = HttpStatus.NO_CONTENT)
//   public String excluirPorId(@PathVariable long id, @PathVariable long id2, RedirectAttributes attr) ){
//	   
//		try {
//			sSubCategoria.excluirPorId(id);
//			attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");
//		}	
//		catch (Exception e) {
//			attr.addFlashAttribute("Erro", e.getMessage()); 
//		}		
//		return  "redirect:".concat(urlListar());	
//   }
   

}


