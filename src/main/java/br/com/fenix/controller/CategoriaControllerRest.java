package br.com.fenix.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.api.exceptionhandle.EntidadeNaoEncontratException;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SubCategoriaRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.icontroller.IControleCategoriaRest;

@RestController
@RequestMapping("/categoria")
public class CategoriaControllerRest  extends ControleAbstratoRest<Categoria> implements IControleCategoriaRest  {
 

	@Autowired
    CategoriaRepositorio categoriaRP;
	@Autowired
	SubCategoriaRepositorio subCategoriaRP;
	
	@Autowired
    CategoriaServico categoriaSC;

	public CategoriaControllerRest(GenericRepository<Categoria> repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
	}
	public String nomeSubCadastro() {
     	return ("categoria/cad_subcategoria");
	}	
	
	@GetMapping("/listar/{tipoLancamento}")  
	public ModelAndView listarView(@PathVariable TipoLancamento tipoLancamento) {	
		System.out.println( tipoLancamento);
    	List<CategoriaDTO>  dados = categoriaSC.listaDeCategorias(tipoLancamento)	;	
		return new ModelAndView("categoria/listar_categoria","categoriaDTO", dados) ;		  			  
	}	
	@GetMapping("/CriarPorModelo/{tipoLancamento}")  
	public ModelAndView CriarModelo(@PathVariable TipoLancamento tipoLancamento) {	
		System.out.println( "Criar por Modelo " + tipoLancamento); 
		List<CategoriaDTO>  dados = categoriaSC.listaDeCategorias(tipoLancamento)	;
		if (dados.isEmpty()) {
			categoriaSC.CriarPorModelo(tipoLancamento);
		}
		
		return new ModelAndView("categoria/listar_categoria","categoriaDTO", dados) ;		  			  
	}
/*----------------------- Lista os dados da tabela NAV TAB ------------------ */	
	@GetMapping("/lista_tab/{tipoLancamento}")  
	public ModelAndView listar_TabView(@PathVariable TipoLancamento tipoLancamento) {	
		System.out.println( tipoLancamento);
    	List<CategoriaDTO>  dados = categoriaSC.listaDeCategorias(tipoLancamento)	;
		System.out.println( dados.size());
    	
		return new ModelAndView("categoria/lista_tab","categoriaDTO", dados) ;		  			  
	}

// ---------------------------- SubCategoria ---------------------------------------------------------	
    @GetMapping("{id}/subcategoria/{id2}") 
    @ResponseStatus(code = HttpStatus.OK)	    	    
    public SubCategoria buscarSubCategoriaPorId (@PathVariable  long id2){
    
    	   	   return subCategoriaRP.findById(id2)	    			   
    	   			   .orElseThrow ( () -> new EntidadeNaoEncontratException("Registro não encontrado Id:" + id2));		    			       		    	
    }

    @GetMapping("{id}/subcategoria/editar/{id2}") 
    @ResponseStatus(code = HttpStatus.OK)	
    public ModelAndView editar_sub_item(@PathVariable long id, @PathVariable long id2) {
    	System.out.println("editar subcategoria");
    	SubCategoria subCategoria = buscarSubCategoriaPorId(id2) ;     		

		 return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
    }    

    @GetMapping("{id}/subcategoria/cadastrar") 
    @ResponseStatus(code = HttpStatus.OK)	
    public ModelAndView cadastrar_sub_item(@PathVariable long id) {    	
    	System.out.println("Cadastro subcategoria");
    	SubCategoria subCategoria = new SubCategoria() ;
    	subCategoria.setCategoria( buscarPorId(id));
    	
		 return new ModelAndView("categoria/cad_subcategoria","subCategoria", subCategoria) ;		 		 	
    }    
    

    @PostMapping("/{id}/subcategoria")
    @Transactional
	@ResponseStatus(code = HttpStatus.CREATED) 
    public SubCategoria criarSubCategoria(@PathVariable long id, @Validated @RequestBody SubCategoria subCategoria){
    	System.out.println("Post criar  subcategoria");
    	System.out.println(subCategoria.toString());
    	subCategoria.setCategoria( buscarPorId(id));
        return subCategoriaRP.save (subCategoria);
    }
   
    @PutMapping("/{id}/subcategoria/{idSub}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public SubCategoria atualizar(@PathVariable long id, @Validated @RequestBody SubCategoria subCategoria){
    	System.out.println("Put  subcategoria");
    	
//    	System.out.println(subCategoria);
    	SubCategoria subCatUpt = subCategoriaRP.findById(subCategoria.getId()).get();
    	
    	subCatUpt.setDescricao(subCategoria.getDescricao()); 
    	subCatUpt.setImp_renda(subCategoria.isImp_renda()); 
    	subCatUpt.setDesp_fixa(subCategoria.isDesp_fixa()); 
    	//subCategoria.setCategoria(categoriaRP.findById(id).get());
    	System.out.println(subCategoria.toString());
        return subCategoriaRP.save (subCatUpt);
    }
    @DeleteMapping("/{id}/subcategoria/{id2}")
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluirPorId(@PathVariable long id, @PathVariable long id2){
     	System.out.println("excluir subcategoria");
    	buscarSubCategoriaPorId(id2);
    	subCategoriaRP.deleteById(id2);
    }
 
	@Override
	public Categoria criar(Categoria entidade) {
		// TODO Auto-generated method stub
		return super.criar(entidade);
	}
	
}