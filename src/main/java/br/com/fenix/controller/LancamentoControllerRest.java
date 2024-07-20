package br.com.fenix.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.FormaPgto;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.DetalheLancamentoRepositorio;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.ContaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FavorecidoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FormaPgtoRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.dominio.servico.LancamentoServico;
import br.com.fenix.icontroller.IControleCategoriaRest;
import br.com.fenix.icontroller.IControleLancamentoRest;

@RestController
@RequestMapping("/lancamento")
public class LancamentoControllerRest {


	@Autowired	
	ContaRepositorio contaRP;
	@Autowired
	FormaPgtoRepositorio formaRP; 
	@Autowired
	FavorecidoRepositorio favorecidoRP;
	@Autowired
	CategoriaServico categoriaSC;
	@Autowired
	LancamentoServico lancSC;

	@Autowired
	LancamentoRepositorio lancRP; 
	
	@Autowired
	DetalheLancamentoRepositorio DetlancRP; 
	
	@ModelAttribute("formaPgtos")
	public List<FormaPgto> listaDeFormaPgto() {
		return formaRP.findByOrderByNomeAsc();
	}	
    
	@ModelAttribute("favorecidos")
	public Iterable<Favorecido> listaDeFavorecido() {		
	 return favorecidoRP.findAll();  
	}
	@ModelAttribute("contas")
	public List<Conta> listaDeContas() {
		
		return contaRP.findByOrderByApelidoAsc();
	}	
	
	@ModelAttribute("mesesAno")
	public List<LocalDate> listaMesAno() {
		int year    = LocalDate.now().getYear();
		
		List<LocalDate> todosMeses = new ArrayList<LocalDate>();
		for (int x = 1; x <= 12; x++) {
		   todosMeses.add( LocalDate.of(year, x, 1) );	
		}

	    return todosMeses;
	}
	
	@ModelAttribute("categoriasDTO")
	public ArrayList<CategoriaDTO> listaDeCategorias() {		
	 return categoriaSC.listaDeCategorias(TipoLancamento.D); 
	}
	
/*	@ModelAttribute("LancamentosDTO")
	public List<LancamentoDTO> listaDeLancamentos() {		
	 return lancSC.findAll();  
	}	
 */
    @GetMapping("/cadastrar")  	
    public ModelAndView cadastrar() {       	
		return new ModelAndView("lancamento/cad_lancamento","lancamentoDTO", new LancamentoDTO()) ;		
    }
    @GetMapping("/editar/{id}")  
	public ModelAndView atualizarView(@PathVariable long id) {    
    	System.out.println("Editar " + id  );
		LancamentoDTO lancamentoDTO = lancSC.findDetLanc(id); 
		return new ModelAndView("lancamento/cad_lancamento","lancamentoDTO",lancamentoDTO) ;		
    }
    
    @PostMapping
	@ResponseStatus(code = HttpStatus.CREATED) 
	@Transactional
    public Lancamento Criar(@Validated @RequestBody LancamentoDTO lancDTO){ 
    	System.out.println("Criar lancDTO");
       return lancSC.salvar(lancDTO);
    } 
    @PutMapping
	@ResponseStatus(code = HttpStatus.OK) 
    public Lancamento alterar(@Validated @RequestBody LancamentoDTO lancDTO){ 
    	System.out.println("alterar lancDTO");
    	System.out.println(lancDTO);
       return lancSC.alterar(lancDTO);
    } 
    @GetMapping("/{id}")	    
    public Lancamento buscarPorId (@PathVariable long id){	 
    	   return lancRP.findById(id)
    			   .orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrado Id:" + id));	
    }	   
	        		
	
	@GetMapping("/listar/{mesLancamento}")  
	public ModelAndView listarView(@PathVariable String mesLancamento) {	
		System.out.println( mesLancamento);
    	List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);
		return new ModelAndView("lancamento/listar_lancamento","lancamentosDTO", dados) ;		  			  
	}	
	
	/*----------------------- Lista os dados da tabela NAV TAB ------------------ */	
	@GetMapping("/lista_tab/{mesLancamento}")  
	public ModelAndView listar_TabView(@PathVariable String mesLancamento) {	
	
		List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);
		
		return new ModelAndView("lancamento/lista_tab","lancamentosDTO", dados);
    	
	}
  
    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluirPorId(@PathVariable long id){
    	
    	DetlancRP.deleteById(id);
    }
/*	
	@GetMapping("")  
	public List<LancamentoDTO>  listar() {	
		System.out.println( "Listar");
    	List<LancamentoDTO>  dados = listaDeLancamentos();
    	
		return dados;		  			  
	}	
*/	
	@GetMapping("/mes/{mesLancamento}")  
	public List<LancamentoDTO>  listarPorMes(@PathVariable String mesLancamento) {	
		System.out.println( "Listar");
    	List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);;    	
		return dados;		  			  
	}

	
  /*  @GetMapping("/cadastrar") 
    public ModelAndView cadastrar(LancamentoDTO lanc) {
    	   System.out.println("cadastro");
          return new ModelAndView("lancamento/cadastrar");
    }	
  
    @GetMapping("/editar/{id}")  
	public ModelAndView atualizarView(@PathVariable long id) {    	
		LancamentoDTO lancamentoDTO = lancSC.findById(id); 
		return new ModelAndView("lancamento/cadastrar","lancamentoDTO",lancamentoDTO) ;		  			  
	}
   */ 
  /*  @GetMapping("/{id}")
    public LancamentoDTO buscarPorId (@PathVariable long id){	  	
    		return lancSC.findById(id);		    	
    }
    
   
 */   

}
