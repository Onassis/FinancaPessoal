package br.com.fenix.fi.conta;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fenix.abstrato.controle.ControleAbstrato;
import br.com.fenix.abstrato.controle.ControleAbstratoDTO;
import br.com.fenix.abstrato.controle.IControle;
import br.com.fenix.abstrato.controle.IControleDTO;
import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.AjudaSelect;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoDTO;
import br.com.fenix.fi.moeda.MoedaRepositorio;

@Controller
@RequestMapping("/conta")
public class ContaController  extends ControleAbstratoDTO<Conta,ContaDTO,UUID> implements IControleDTO<Conta,ContaDTO,UUID>   {

  @Autowired
  MoedaRepositorio moedaRP;
  
  @Autowired
  ContaServico servico; 

	public ContaController() {
		super();
	}
	
	@Override
	public ServicoAbstratoDTO getServico() {
		
		return servico;
	}

	@ModelAttribute("tipoConta")
	public List<Option>  listaTipoConta() {
		return TipoConta.listaTipoConta();
	}
	
	@ModelAttribute("moedas")
	@Cacheable("moeda")
	public List<Option> listaDeMoedas() {
		System.out.println("contacontrol");
		
//		 List<AjudaSelect> selecoptions = moedaRP.findAjudaSelectAll();
		 
		 List<Option> options =  moedaRP.findOptionAll();
		 
//		List<Option> options  =  moedaRP.findOptionAll();
//		 List<Option> options  =  moedaRP.findAllByOrderByCodigoAsc().stream()	               
//	                .map(p -> new Option(p.getCodigo(), p.getMoeda()))
//	                .collect(Collectors.toList());
		 
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
