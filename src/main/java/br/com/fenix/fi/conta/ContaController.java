package br.com.fenix.fi.conta;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import br.com.fenix.abstrato.ControleAbstrato;
import br.com.fenix.abstrato.IControle;
import br.com.fenix.abstrato.ServicoAbstrato;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

@RestController
@RequestMapping("/conta2")
public class ContaController  extends ControleAbstrato<Conta,Long> implements IControle<Conta,Long>   {

  @Autowired
  MoedaRepositorio moedaRP;
  @Autowired
  ContaRepositorio contaRp; 
//ContaService servico; 
	
	public ContaController( ContaService servico) {
		super(servico);
		// TODO Auto-generated constructor stub
	}

//	@ModelAttribute("moedas")
//	@Cacheable("moeda")
//	public List<Moeda> listaDeMoedas() {
//		return moedaRP.findAllByOrderByCodigoAsc();
//	}	
	@ModelAttribute("moedas")
	@Cacheable("moeda")
	public List<Option> listaDeMoedas() {
		System.out.println("contacontrol");
//		 List<Option> options = Arrays.asList( 
//		            new Option("1", "Option 1"),
//		            new Option("2", "Option 2"),
//		            new Option("3", "Option 3")
//		        );
		 List<Option> options  = moedaRP.findAllByOrderByCodigoAsc().stream()	               
	                .map(p -> new Option(p.getCodigo(), p.getMoeda()))
	                .collect(Collectors.toList());
		 
//		 List<Option> options = moedaRP.findAllByOrderByCodigoAsc() 
//				 .forEach(new Option(moeda.getCodigo,moeda.getAjuda));
					
		 return options;
		//return moedaRP.findAllByOrderByCodigoAsc();
	}	

}
