package br.com.fenix.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.abstrato.IControleRest;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Automacao;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.AutomacaoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.favorecido.Favorecido;
import br.com.fenix.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.icontroller.IControleFavorecidoRest;
import jakarta.annotation.security.RolesAllowed;

@RestController
@PreAuthorize("hasRole('USER')")    
@RequestMapping("/automacao")
public class AutomacaoControllerRest  extends ControleAbstratoRest<Automacao> implements IControleRest<Automacao> {
 
//	@Autowired
	AutomacaoRepositorio AutomacaoRP;
	@Autowired
	ContaRepositorio contaRP;
	@Autowired
	FavorecidoRepositorio favorecidoRP;
	@Autowired
	CategoriaServico categoriaSC;
    	
	public AutomacaoControllerRest(AutomacaoRepositorio repositorio) {
		super(repositorio);
	}
	
	@ModelAttribute("contas")
	public List<Conta> listaDeContas() {
		return contaRP.findByOrderByApelidoAsc();
				
	}
	@ModelAttribute("favorecidos")
	public Iterable<Favorecido> listaDeFavorecido() {		
	 return favorecidoRP.findAll();  
	}
   @ModelAttribute("categoriasDTO")
	public ArrayList<CategoriaDTO> listaDeCategorias() {		
	 return categoriaSC.listaDeCategorias(null); 
	}

}
