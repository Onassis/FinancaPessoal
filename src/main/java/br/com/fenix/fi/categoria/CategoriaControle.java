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
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaControle extends 	ControleAbstratoDTO<Categoria,CategoriaDTO,Long> 
			implements IControleDTO<Categoria,CategoriaDTO,Long>   {

	@Autowired
	ContaServico contaSC;
	@Autowired
	CategoriaServico servico;
	
	//private GenericConverter<Categoria, CategoriaDTO> converter;

	public CategoriaControle() {
	  //	this.converter = new GenericConverter<>(Categoria.class, CategoriaDTO.class);
		
	}
	@Override
	public ServicoAbstratoDTO getServico() {
	
		return servico;
	}
	@ModelAttribute("opTiposLancamento")	
    public List<Option> getOptionsFromTipoLancamento() {
        return Stream.of(TipoLancamento.values())
                     .map(tipo -> new Option(tipo.getTipoLancamento(), tipo.getDescricao()))
                     .collect(Collectors.toList());
    }

	@ModelAttribute("opContas")
	@Cacheable(value="conta", sync = true)
	public List<Option>  listaDeContas() {
		   return contaSC.listaDeContas(TipoConta.CC); 		
	}
	
   @ModelAttribute("categoriasDTO")
   @Cacheable(value="categoria", sync = true)
	public List<Option> listaDeCategorias() {	   
	   return servico. listaDeCategoriasOpt(TipoLancamento.D); 
	}

   @GetMapping("/listar/{tipoLancamento}")  
	public ModelAndView listarView(@PathVariable TipoLancamento tipoLancamento) {	
		System.out.println( tipoLancamento);
		List<CategoriaDTO>  dados = servico.listaDeCategorias(tipoLancamento)	;	
		return new ModelAndView("categoria/listar_categoria","categoriaDTO", dados) ;		  			  
	}
	


/*----------------------- Lista os dados da tabela NAV TAB ------------------ */	
	@GetMapping("/lista_tab/{tipoLancamento}")  
	public ModelAndView listar_TabView(@PathVariable TipoLancamento tipoLancamento) {	
		System.out.println( tipoLancamento);
   	List<CategoriaDTO>  dados = servico.listaDeCategorias(tipoLancamento)	;
		System.out.println( dados.size());
   	
		return new ModelAndView("categoria/lista_tab","categoriaDTO", dados) ;		  			  
	}

//---------------------------- SubCategoria ---------------------------------------------------------	
   @GetMapping("{id}/subcategoria/{id2}") 
   @ResponseStatus(code = HttpStatus.OK)	    	    
   public CategoriaDTO buscarSubCategoria (@PathVariable  long id2){
          return servico.buscarSubCategoriaPorId(id2);
//   	   	   return servico.findById(id2)	    			   
//   	   			   .orElseThrow ( () -> new RegistroNaoExisteException("Registro não encontrado Id:" + id2));		    			       		    	
   }

   @GetMapping("{id}/subcategoria/editar/{id2}") 
   @ResponseStatus(code = HttpStatus.OK)	
   public ModelAndView editar_sub_item(@PathVariable long id, @PathVariable long id2) {
   	System.out.println("editar subcategoria");
   	   CategoriaDTO subCategoria = servico.buscarSubCategoriaPorId(id2);     		

		 return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
   }    

   @GetMapping("{id}/subcategoria/cadastrar") 
   @ResponseStatus(code = HttpStatus.OK)	
   public ModelAndView cadastrar_sub_item(@PathVariable long id) {    	
   	System.out.println("Cadastro subcategoria");
   	SubCategoria subCategoria = new SubCategoria() ;
   	subCategoria.setCategoria( servico.buscarPorId(id).get());
   	
		 return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
   }    
   

   @PostMapping("/{id}/subcategoria")
   @Transactional
	@ResponseStatus(code = HttpStatus.CREATED) 
   public CategoriaDTO criarSubCategoria(@PathVariable long id, @Validated @RequestBody CategoriaDTO subCategoria) throws Exception{
   	System.out.println("Post criar  subcategoria");
   	System.out.println(subCategoria.toString());
//   	subCategoria.setIdCategoria(servico.buscarPorId(id).get());
   	
   	return (CategoriaDTO) getServico().criarDTO(subCategoria); 
   	
    //   return (SubCategoria) getServico().criar(subCategoria);
   }
  
   @PutMapping("/{id}/subcategoria/{idSub}")
   @Transactional
   @ResponseStatus(code = HttpStatus.OK)
   public SubCategoria atualizar(@PathVariable long id, @Validated @RequestBody CategoriaDTO subCategoria){
   	System.out.println("Put  subcategoria");
   	return null;   	
////   	System.out.println(subCategoria);
//   	SubCategoria subCatUpt = subCategoriaRP.findById(subCategoria.getId()).get();
//   	
//   	subCatUpt.setDescricao(subCategoria.getDescricao()); 
//   	subCatUpt.setImp_renda(subCategoria.isImp_renda()); 
//   	subCatUpt.setDesp_fixa(subCategoria.isDesp_fixa()); 
//   	//subCategoria.setCategoria(categoriaRP.findById(id).get());
//   	System.out.println(subCategoria.toString());
//       return subCategoriaRP.save (subCatUpt);
   }
   @DeleteMapping("/{id}/subcategoria/{id2}")
   @Transactional
   @ResponseStatus(code = HttpStatus.NO_CONTENT)
   public void excluirPorId(@PathVariable long id, @PathVariable long id2){
    	System.out.println("excluir subcategoria");
    	
////   	buscarSubCategoriaPorId(id2);
//    getServico().de
//   	subCategoriaRP.deleteById(id2);
   }



}
