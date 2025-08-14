package br.com.fenix.fi.lancamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fenix.abstrato.controle.ControleAbstratoDTO;
import br.com.fenix.abstrato.controle.IControleDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.categoria.CategoriaDTO;
import br.com.fenix.fi.categoria.CategoriaServico;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.fi.conta.ContaServico;
import br.com.fenix.fi.detalheLancamento.DetalheLancServico;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.formaPgto.FormaPgto;
import br.com.fenix.fi.formaPgto.FormaPgtoRepositorio;
import br.com.fenix.fi.subCategoria.SubCategoriaRepositorio;
import br.com.fenix.fi.subCategoria.SubCategoriaServico;

@Controller
@RequestMapping("/lancamento")
public class LancamentoController extends 	ControleAbstratoDTO<Lancamento,LancamentoDTO,Long>  implements IControleDTO<Lancamento,LancamentoDTO,Long> {


	@Autowired	
	ContaRepositorio contaRP;
	@Autowired
	FavorecidoRepositorio favorecidoRP;
	@Autowired
	CategoriaServico categoriaSC;
	@Autowired
	SubCategoriaRepositorio subCategoriaRP;
	
	@Autowired
	LancamentoServico lancSC;
	
	@Autowired
	DetalheLancServico detLancSC;
	
   @Autowired
   ConverterLancamentoFactory converters;
	
	@Override
	public LancamentoServico getServico() {
	 	return lancSC;
	}
	
	@Cacheable("favorecidos")
	@ModelAttribute("favorecidos")
	public Iterable<Option> listaDeFavorecido() {	
		 return favorecidoRP.findOption();  
	}

	@Cacheable(value = "contas")
	@ModelAttribute("contas")
	public List<Option> listaDeContas() {	
		return contaRP.findOption(); 
	}	
	@Cacheable("contasCorrente")
	@ModelAttribute("contasCorrente")
	public List<Option> listaDeContasCorrente() {	
		return contaRP.findOptionByTipoConta(TipoConta.CC);
	}
	@Cacheable("cartaoCredito")
	@ModelAttribute("cartaoCredito")
	public List<Option> listaDeCartaoCredito() {	
		return contaRP.findOptionByTipoConta(TipoConta.CR);
	}
   
	@ModelAttribute("tipoOperacaoOp")
	public List<Option> listaTipoOperacao() { 
		return TipoOperacao.listaTipoOperacao(); 
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
	@Cacheable("subCategoriasDebito")
	@ModelAttribute("subCategoriasDebito")
	public List<Option> listaDeCategoriasDebitos() {			
//		 return categoriaSC.listaDeCategoriasOpt(TipoLancamento.D);	
		 return subCategoriaRP.findOptionByTipoCategoriaOrderByDescricaoAsc(TipoLancamento.D); 
	}
	@Cacheable("subCategoriasCredito")
	@ModelAttribute("subCategoriasCredito")
	public List<Option> listaDeCategoriasCredito() {			
//		 return categoriaSC.listaDeCategoriasOpt(TipoLancamento.C);		
		 return subCategoriaRP.findOptionByTipoCategoriaOrderByDescricaoAsc(TipoLancamento.C); 
	}
	
	@GetMapping("/listar")
	@Override
	public String  listarView(ModelMap model) {
		LocalDateTime now = LocalDateTime.now(); 
		String mesLancamento = now.getMonth().toString() + "/" + now.getDayOfYear();  
//	    List<DetalheLancDTO> dtos = detLancSC.listaPorMesAno(mesLancamento);
    	List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);
		model.addAttribute(nomeClasseDTO(), dados);
		return listarHtml();
	}	

	@GetMapping("/listar/{mesLancamento}")  
	public ModelAndView listarView(@PathVariable String mesLancamento) {	
		System.out.println( mesLancamento);
    	List<LancamentoDTO>  dados = lancSC.listaPorMesAno(mesLancamento);
//    	List<DetalheLancDTO> dados = detLancSC.listaPorMesAno(mesLancamento);

    	return new ModelAndView("lancamento/listar_lancamento","lancamentoDTO", dados) ;		  			  
	}
	
	@GetMapping("/cadastrar/{tipoOperacao}")
	public String cadastrar(@PathVariable TipoOperacao tipoOperacao, LancamentoDTO dto) {
		dto.setTipoOperacao(tipoOperacao);
		return converters.getCadastro(tipoOperacao.toString()) ;
	}
	@Override
	@GetMapping("/cadastrar")
	public String cadastrar(LancamentoDTO dto) {
		
		TipoOperacao tipoOperacao = dto.getTipoOperacao();  
		if (tipoOperacao == null) {
			tipoOperacao = TipoOperacao.DB;
			dto.setTipoOperacao(tipoOperacao);				
		}
		return converters.getCadastro(tipoOperacao.toString()) ;
	} 
	@Override
	@GetMapping("/editar/{id}")
	public String atualizarView(Long id, ModelMap model, RedirectAttributes attr) {
		
		 TipoOperacao tipoOperacao =null;
		 if (model.containsAttribute("Erro")) {
			 
			   LancamentoDTO dto = (LancamentoDTO) model.getAttribute(nomeClasseDTO() ) ;
				String mesAno = dto.getMesAnoLancamento();
			    tipoOperacao = dto.getTipoOperacao();
			   
			   return converters.getCadastro(tipoOperacao.toString()) ;
		 }
		 try {
		    LancamentoDTO dto =  detLancSC.buscaDTOPorId(id); 
		    tipoOperacao = dto.getTipoOperacao();
		    
		   model.addAttribute(nomeClasseDTO() , dto);
		 } catch (RegistroNaoExisteException e) {
			 attr.addFlashAttribute("Erro", e.getMessage());   	
			 return "redirect:".concat(urlListar()); 
			 }		
	   return converters.getCadastro(tipoOperacao.toString()) ;
	}
	@Override
	public String alterar(LancamentoDTO dto, RedirectAttributes attr) {
		String mesAno = dto.getMesAnoLancamento();
		try {	
//			dto = getServico().atualizarDTO(dto);
	        
			dto = detLancSC.atualizarDTO(dto); 
	        
			attr.addFlashAttribute("Sucesso", "Registro alterardo com sucesso.");
		}
		catch (Exception e) {
			System.out.println("ControleAbstrato-> Salvar -> Exception");
			attr.addFlashAttribute("Erro", e.getMessage());			
			attr.addFlashAttribute(nomeClasseDTO(), dto );			
			return "redirect:".concat(urlEditar(  dto.getId()));		
			}
		 			
		return "redirect:".concat(urlListar()) 
				.concat("/")
				.concat(mesAno);	
	}
	@Override
	@GetMapping("/excluir/{id}")   
	public String excluirPorId(@PathVariable Long id, RedirectAttributes attr) {
		LocalDate dataAtual = LocalDate.now() ;
		String mesAno = String.format("%02d",dataAtual.getMonthValue())   
				.concat("/")
				.concat(String.valueOf(dataAtual.getYear()));
		try {
			LancamentoDTO dto = getServico().buscaDTOPorId(id);
			mesAno = dto.getMesAnoLancamento();
			getServico().excluirPorId(id);
			attr.addFlashAttribute("Sucesso", "Registro excluido com sucesso.");
		}	
		catch (Exception e) {
			attr.addFlashAttribute("Erro", e.getMessage()); 
		}		
		return "redirect:".concat(urlListar()) 
				.concat("/")
				.concat(mesAno);
	}

}
