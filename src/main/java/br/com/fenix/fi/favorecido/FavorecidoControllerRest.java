package br.com.fenix.fi.favorecido;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.fenix.abstrato.controle.ControleAbstratoRest;
import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.CategoriaRepositorio;
import br.com.fenix.fi.categoria.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.subCategoria.SubCategoria;
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
