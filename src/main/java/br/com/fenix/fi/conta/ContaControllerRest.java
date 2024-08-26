package br.com.fenix.fi.conta;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

@RestController
@RequestMapping("/api/conta")
public class ContaControllerRest  extends ControleAbstratoRest<Conta> implements IControleContaRest  {

    @Autowired
    MoedaRepositorio moedaRP;
    @Autowired
    ContaService contaSV; 
    @Autowired
    private ContaRepositorioRest contaRP; 
    
	public ContaControllerRest(ContaRepositorioRest repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
	}
	@ModelAttribute("moedas")
	@Cacheable("moeda")
	public List<Moeda> listaDeMoedas() {
		return moedaRP.findAllByOrderByCodigoAsc();
	}	
    @Override 
    @CachePut("conta")
    public Conta criar(Conta conta) {
    	System.out.println("Conta" + conta);
    	contaSV.antesDeSalvar(conta);
    	return super.criar(conta); 	
    }
    @Override
    @CachePut("conta")
    public Conta atualizar(Conta conta) {
    	System.out.println("Conta" + conta);
    	contaSV.antesDeSalvar(conta);
    	return super.atualizar(conta); 	
    }
   
 		@GetMapping("/listar2")  
 		public ModelAndView listarView2(Conta entidade) {
 	    	System.out.println("Listar");
 	    	
 			Iterable<Conta> dados = contaRP.findAll();
 			return new ModelAndView("conta/listar_conta2",nomeEntidade(entidade),dados) ;		  			  
 		}
	

}
