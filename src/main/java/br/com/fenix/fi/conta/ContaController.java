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
import br.com.fenix.abstrato.ControleAbstrato;
import br.com.fenix.abstrato.IControle;
import br.com.fenix.abstrato.ServicoAbstrato;
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
	@Cacheable("moedas")
	public List<Moeda> listaDeMoedas() {
		return moedaRP.findAllByOrderByCodigoAsc();
	}	

}
