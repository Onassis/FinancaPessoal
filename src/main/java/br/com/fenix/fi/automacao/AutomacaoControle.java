package br.com.fenix.fi.automacao;


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

import br.com.fenix.abstrato.controle.ControleAbstrato;
import br.com.fenix.abstrato.controle.IControle;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.CategoriaRepositorio;
import br.com.fenix.fi.categoria.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.favorecido.FavorecidoServico;
import br.com.fenix.fi.moeda.MoedaRepositorio;
import br.com.fenix.fi.subCategoria.SubCategoriaRepositorio;

@Controller
@RequestMapping("/automacao")
public class AutomacaoControle  extends ControleAbstrato<AutomacaoServico,Automacao,Long> implements IControle<Automacao,Long>   {
  
	@Autowired
	FavorecidoServico favorecidoSC;
	@Autowired
	SubCategoriaRepositorio subCategoriaRP;
	
	@Autowired
	ContaServico contaSC;

	public AutomacaoControle( AutomacaoServico servico) {
		super(servico);
	}	
	@ModelAttribute("tipoOperacao")
	public List<Option>  listaTipoOperacao() {
		return TipoOperacao.listaTipoOperacao();
	}
	@ModelAttribute("contas")
	public List<Option>  listaDeContas() {
		return contaSC.listaDeContas(TipoConta.CC); 			
	}
	
	@ModelAttribute("favorecidos")
	public List<Option>  listaDeFavorecido() {
		  return favorecidoSC.listaDeFavorecido(); 			
	}	
   @ModelAttribute("categoriasDTO")	
	public List<Option>  listaDeCategorias() {

	      return subCategoriaRP.findAllOption();
	}	
}
