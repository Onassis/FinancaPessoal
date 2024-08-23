package br.com.fenix.fi.conta;


import java.util.ArrayList;
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
import br.com.fenix.dominio.dto.OptionLong;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

@RestController
@RequestMapping("/conta")
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

	@ModelAttribute("moedas")
	@Cacheable("moeda")
	public List<Option> listaDeMoedas() {
		System.out.println("contacontrol");
		 List<Option> options  = moedaRP.findAllByOrderByCodigoAsc().stream()	               
	                .map(p -> new Option(p.getCodigo(), p.getMoeda()))
	                .collect(Collectors.toList());
		 
		 return options;
	}	
	
	@ModelAttribute("diasMesLong")
	public List<OptionLong> listaDias() {
		System.out.println("listaDias");
		 List<OptionLong> options = new ArrayList<>();

		System.out.println("contacontrol");
		for (int i=1;i<=31;i++) { 
			 new Option(i, i);
		}
		 return options;
	}
	@ModelAttribute("diaMes")
	public List<Option> listaDiasMes() {
		System.out.println("contaDiaMes");
		 List<Option> options  = new ArrayList<>();
		 for (int i = 1; i <= 31; i++) {
			   options.add( new Option(i, i)); 
		 }					
		 return options;
	}
}
