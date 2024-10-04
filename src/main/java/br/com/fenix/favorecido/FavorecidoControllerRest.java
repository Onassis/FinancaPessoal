package br.com.fenix.favorecido;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.icontroller.IControleFavorecidoRest;
import jakarta.annotation.security.RolesAllowed;

@RestController
@PreAuthorize("hasRole('USER')")    

//@SessionAttributes("categoriasDTO")
@RequestMapping("api/favorecido")
public class FavorecidoControllerRest  extends ControleAbstratoRest<Favorecido> implements IControleFavorecidoRest  {
 
//	@Autowired
//	FavorecidoRepositorio FavorecidoRP;
	@Autowired
	ContaRepositorio contaRP;
	@Autowired
	CategoriaServico categoriaSC;
    	
	public FavorecidoControllerRest(FavorecidoRepositorio repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
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
