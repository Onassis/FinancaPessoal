package br.com.fenix.fi.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.abstrato.controle.ControleAbstratoDTO;
import br.com.fenix.abstrato.controle.IControleDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.formaPgto.FormaPgto;
import br.com.fenix.fi.formaPgto.FormaPgtoRepositorio;

@Controller
@RequestMapping("/lancamento")
public class LancamentoController 
//extends 	ControleAbstratoDTO<Lancamento,LancamentoDTO,Long> 
//			implements IControleDTO<Lancamento,LancamentoDTO,Long>   
{


	@Autowired	
	ContaRepositorio contaRP;
	@Autowired
	FormaPgtoRepositorio formaRP; 
	@Autowired
	FavorecidoRepositorio favorecidoRP;
	@Autowired
	CategoriaServico categoriaSC;
	
	@Autowired
	LancamentoServico lancSC;;
	@Autowired
	DetalheLancServico detLancSC;;
//	@Override
//	public LancamentoServico getServico() {
//		// TODO Auto-generated method stub
//		return lancSC;
//	}
	@ModelAttribute("formaPgtos")
	public List<FormaPgto> listaDeFormaPgto() {
		return formaRP.findByOrderByNomeAsc();
	}	
    
	@ModelAttribute("favorecidos")
	public Iterable<Favorecido> listaDeFavorecido() {		
	 return favorecidoRP.findAll();  
	}
	@ModelAttribute("contas")
	public List<Conta> listaDeContas() {		
		return contaRP.findByOrderByApelidoAsc();
	}	
	
	@ModelAttribute("mesesAno")
	public List<LocalDate> listaMesAno() {
		int year    = LocalDate.now().getYear();
		
		List<LocalDate> todosMeses = new ArrayList<LocalDate>();
		for (int x = 1; x <= 12; x++) {
		   todosMeses.add( LocalDate.of(year, x, 1) );	
		}

	    return todosMeses;
	}
	
	@ModelAttribute("categoriasDTO")
	public List<CategoriaDTO> listaDeCategorias() {		
	 return categoriaSC.listaDeCategorias(TipoLancamento.D); 
	}
	

	@GetMapping("/listar/{mesLancamento}")  
	public ModelAndView listarView(@PathVariable String mesLancamento) {	
		System.out.println( mesLancamento);
//    	List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);
    	List<DetalheLancDTO> dados = detLancSC.listaPorMesAno(mesLancamento);

    	return new ModelAndView("lancamento/listar_lancamento","DetalheLancDTO", dados) ;		  			  
	}

}
