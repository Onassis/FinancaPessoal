package br.com.fenix.controller;


import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import br.com.fenix.abstrato.ControleAbstratoRest;
import br.com.fenix.abstrato.IControleRest;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.dto.UploadDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.Lancamento;

import br.com.fenix.dominio.modelo.DadoBasico.Automacao;
import br.com.fenix.dominio.modelo.DadoBasico.FormaPgto;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.LancAuxRepositorio;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.AutomacaoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.FormaPgtoRepositorio;
import br.com.fenix.dominio.servico.CategoriaServico;
import br.com.fenix.dominio.servico.LancAuxServico;
import br.com.fenix.dominio.servico.LancamentoServico;
import br.com.fenix.favorecido.Favorecido;
import br.com.fenix.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.conta.ContaRepositorio;
import br.com.fenix.icontroller.IControleCategoriaRest;
import br.com.fenix.icontroller.IControleLancamentoRest;
import br.com.fenix.util.Coletor;

@PreAuthorize("hasRole('USER')") 
@Controller
@RequestMapping("/upload")
public class UploadControllerRest  { 

	@Autowired	
	ContaRepositorio contaRP;
	@Autowired
	FormaPgtoRepositorio formaRP; 
	@Autowired
	FavorecidoRepositorio favorecidoRP;
	@Autowired
	CategoriaServico categoriaSC;

	@Autowired
	LancamentoServico lancSC;
	@Autowired
	LancAuxServico lancAuxSC;
	@Autowired
	LancamentoRepositorio lancRP; 
	
	@Autowired
	LancAuxRepositorio lancAuxRP; 
	
	
	
	@ModelAttribute("mesesAno")
	public List<String> listaMesAno() {
		LocalDate data   = LocalDate.now().minusMonths(2);
		String sData; 
		
		List<String> todosMeses = new ArrayList<String>();
		for (int x = 0; x <= 6; x++) {
			sData = String.format("%02d",data.getMonthValue());
			sData = sData.concat("/");
			sData = sData.concat(String.format("%04d",data.getYear()));
			todosMeses.add( sData );
			data = data.plusMonths(1);			
		}

	    return todosMeses;
	}
	/*
	 * 	@ModelAttribute("formaPgtos")
	public List<FormaPgto> listarFormaPgto() {
		return formaRP.findByOrderByNomeAsc();
	}  
	@ModelAttribute("favorecidos")
	public Iterable<Favorecido> listarFavorecido() {		
	 return favorecidoRP.findAll();  
	}
	
	@ModelAttribute("contas")
	public List<Conta> listarConta() {		
		return contaRP.findByTipoContaOrderByApelidoAsc(TipoConta.CC);
	}	
 */	
	@ModelAttribute("contaCartao")
	public List<Conta> listarCartao() {		
		return contaRP.findByTipoContaOrderByApelidoAsc(TipoConta.CR);
	}
  
    @GetMapping
	public ModelAndView listarUploadView() {
		// TODO Auto-generated method stub    	
    	UploadDTO dado = new UploadDTO();		
		return new ModelAndView("upload/upload","upload",dado) ;		  			  
	}	
    @GetMapping("/Cartao")
	public ModelAndView listarUploadCSVView() {
		// TODO Auto-generated method stub    	
    	UploadDTO dado = new UploadDTO();		
		return new ModelAndView("upload/uploadCSV","upload",dado) ;		  			  
	}	
    @GetMapping("/confirmar")
	public ModelAndView listarUploadViewConf(LancamentoDTO entidade) {
		// TODO Auto-generated method stub
    	
		Iterable<LancAux> dados = lancAuxRP.findAll();
		
		return new ModelAndView("upload/listar_lancamento","lancamentosDTO",dados) ;
//		return null; 
	}	
    @GetMapping("/confirmarCartao")
	public ModelAndView listarUploadViewConfCartao(LancamentoDTO entidade) {
		// TODO Auto-generated method stub
    	
		Iterable<LancAux> dados = lancAuxRP.findAll();
		
		return new ModelAndView("upload/listar_lancamentoCartao","lancamentosDTO",dados) ;
//		return null; 
	}
    
    @GetMapping("/gerar")
    @Transactional
	public ModelAndView salvarUpload() {	    	
    	ArrayList<LancAux> dados = lancAuxRP.findAll();
    	lancAuxSC.gerarLancamento(dados);
		return new ModelAndView("upload/listar_lancamento","LancAux",dados) ;
	}	
 	
      @Transactional
	  @PostMapping  	  
	  public String FileUpload(@RequestParam("conta") long  contaId, @RequestParam("file") MultipartFile file ) throws IOException {
    	   System.out.println("handleFileUpload");
		    String fileName = file.getOriginalFilename();
		    List<String> conteudo =  readAll(file.getInputStream()); 
		    Optional<Conta> contaImp  = Optional.ofNullable(contaRP.findById(contaId).orElseThrow(() -> new RegistroNaoExisteException("Conta não cadastrada")));;
		    Conta conta = contaImp.get(); 
		    Coletor lancamento  = lancAuxSC.processaOFX(conta,conteudo ) ;
		    lancAuxSC.excluiSalvaTodos(lancamento.getLancamentosAux())	;		         
		    return "redirect:/upload/confirmar";
	  }
      
      @Transactional
	  @PostMapping("/Cartao")  	  
	  public String FileUploadCSV(@RequestParam("conta") long  contaId,
			  					 @RequestParam("mesCarga") String  mesCarga,
			  					 @RequestParam("saldoIni") BigDecimal  saldoIni,
			  					 @RequestParam("file") MultipartFile file ) throws IOException {
     
    	   System.out.println("handleFileUploadCSV");
		    String fileName = file.getOriginalFilename();
		    List<String> conteudo =  readAll(file.getInputStream()); 
		    
		    Optional<Conta> contaImp  = Optional.ofNullable(contaRP.findById(contaId).orElseThrow(() -> new RegistroNaoExisteException("Conta não cadastrada")));;
		    Conta conta = contaImp.get(); 
		    Coletor lancamento = lancAuxSC.processaCartao(conta,mesCarga,saldoIni,conteudo ) ;
		    lancAuxSC.excluiSalvaTodos(lancamento.getLancamentosAux())	;
		         
		    return "redirect:/upload/confirmarCartao";
	  }

		private List<String> readAll(InputStream is) throws IOException {
			List<String> conteudo = new ArrayList<String>();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    String line = null;
		    String ls = System.getProperty("line.separator");
		    try{
		        while((line=reader.readLine())!=null){
		        	conteudo.add(line);
		        }
		        return conteudo;
		    }finally{
		        reader.close();
		    }
		}
}
