package br.com.fenix.fi.conta;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fenix.abstrato.ControleAbstrato;
import br.com.fenix.abstrato.IControle;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

@Controller
@RequestMapping("/conta")
public class ContaController  extends ControleAbstrato<ContaService,Conta,Long> implements IControle<Conta,Long>   {

  @Autowired
  MoedaRepositorio moedaRP;

	
	public ContaController( ContaService servico) {
		super(servico);
		this.servico = servico;
	}
	
	@ModelAttribute("tipoConta")
	public List<Option>  listaTipoConta() {
		   List<Option> options = Stream.of(TipoConta.values())
		            .map(tipo -> new Option(tipo.name(), tipo.getDescricao()))
		            .collect(Collectors.toList());
		return options;
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
