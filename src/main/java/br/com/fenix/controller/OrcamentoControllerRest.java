package br.com.fenix.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.abstrato.IControleRest;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.dto.OrcamentoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.Orcamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.FormaPgto;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.OrcamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FavorecidoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FormaPgtoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SubCategoriaRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.dominio.servico.LancamentoServico;
import br.com.fenix.dominio.servico.OrcamentoServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.icontroller.IControleCategoriaRest;
import br.com.fenix.icontroller.IControleLancamentoRest;
import br.com.fenix.icontroller.IControleOrcamentoRest;

@RestController
@RequestMapping("/orcamento")
public class OrcamentoControllerRest  extends ControleAbstratoRest<Orcamento>  implements IControleOrcamentoRest{

	@Autowired
	OrcamentoServico orcamentoSR;
	
	public OrcamentoControllerRest(GenericRepository<Orcamento> repositorio) {
		super(repositorio);
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/listar/{ano}")  
	public ModelAndView listarAno(@PathVariable int ano) {	

    	List<OrcamentoDTO>  dados = orcamentoSR.listaOrcamentoPorAno(ano)	;
    	System.out.println("Tamanho orcacamento " + dados.size());
		return new ModelAndView("orcamento/listar_orcamento","orcamentoDTO", dados) ;		  			  
	}
	@GetMapping("/listarjsf/{ano}")  
	public ModelAndView listarAnojsf(@PathVariable int ano) {	 
		List<OrcamentoDTO>  dados = orcamentoSR.listaOrcamentoPorAno(ano)	;
    	System.out.println("Tamanho orcacamento " + dados.size());
		return new ModelAndView("orcamento/listar_orc.jsf","orcamentoDTO", dados) ;	  			  
	}
	
	@GetMapping("/listarNode")  
	public ModelAndView listarAnoNode() {	 
		return new ModelAndView("orcamento/cad_orcamento") ;		  			  
	}
    @GetMapping("/listarNode/{ano}") 
    public List<Node> nodes(@PathVariable int ano) {
        return orcamentoSR.getSampleNodeList(ano, TipoLancamento.D);
    }
    
	@GetMapping("/ano/{ano}")  
	public List<OrcamentoDTO>  listarAno1(@PathVariable int ano) {	

    	return  orcamentoSR.listaOrcamentoPorAno(ano)	;
    	
				  			  
	}	
	

	

	

	


}
