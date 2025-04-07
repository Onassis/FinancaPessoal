package br.com.fenix.fi.favorecido;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fenix.abstrato.controle.ControleAbstrato;
import br.com.fenix.abstrato.controle.ControleAbstratoDTO;
import br.com.fenix.abstrato.controle.IControle;
import br.com.fenix.abstrato.controle.IControleDTO;
import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;

@Controller
@RequestMapping("/favorecido")
public class FavorecidoControle extends ControleAbstratoDTO<Favorecido,FavorecidoDTO,Long> 
								implements IControleDTO<Favorecido,FavorecidoDTO,Long>   {
	@Autowired
	ContaRepositorio contaRP;
	@Autowired
	CategoriaServico categoriaSC;
	@Autowired
	FavorecidoServico servico;
	
	public FavorecidoControle() {
		super();
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
@Override
public ServicoAbstratoDTO getServico() {
	// TODO Auto-generated method stub
	return this.servico;
}


}
