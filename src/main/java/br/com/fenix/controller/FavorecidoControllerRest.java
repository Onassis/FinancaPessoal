package br.com.fenix.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FavorecidoRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.icontroller.IControleFavorecidoRest;
import jakarta.annotation.security.RolesAllowed;

@RestController
@PreAuthorize("hasRole('USER')")    

//@SessionAttributes("categoriasDTO")
@RequestMapping("/favorecido")
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
	public List<Conta> listaDeContas() {
		return contaRP.findByTipoContaOrderByApelidoAsc(TipoConta.CC);
	}
	
   @ModelAttribute("categoriasDTO")
   @Cacheable(value="categoria", sync = true)
	public ArrayList<CategoriaDTO> listaDeCategorias() {		
	 return categoriaSC.listaDeCategorias(TipoLancamento.D); 
	}

}
