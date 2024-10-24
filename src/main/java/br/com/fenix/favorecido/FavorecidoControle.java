package br.com.fenix.favorecido;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fenix.abstrato.ControleAbstrato;
import br.com.fenix.abstrato.IControle;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;

@Controller
@RequestMapping("/favorecido")
public class FavorecidoControle extends ControleAbstrato<FavorecidoServico,Favorecido,Long> implements IControle<Favorecido,Long>   {
	@Autowired
	ContaRepositorio contaRP;
	@Autowired
	CategoriaServico categoriaSC;

	public FavorecidoControle(FavorecidoServico servico) {
		super(servico);
		
	}


	@ModelAttribute("contas")
	@Cacheable(value="conta", sync = true)
	public List<Option>  listaDeContas() {
		   List<Option> options = contaRP.findByTipoContaOrderByApelidoAsc(TipoConta.CC).stream()    
				.map(conta -> new Option(conta.getId(), conta.getAjuda()))
	            .collect(Collectors.toList());
			return options;
			
	}
	
   @ModelAttribute("categoriasDTO")
   @Cacheable(value="categoria", sync = true)
	public List<Option> listaDeCategorias() {	
	   List<Option> options =  categoriaSC.listaDeCategorias(TipoLancamento.D).stream()
				.map(categoria -> new Option(categoria.getId(), categoria.getDescricao()))
	            .collect(Collectors.toList());
			   			   			   
	   return options;
	}
}
