package br.com.fenix.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.repositorio.dadosBasico.ContaRepositorio;
import br.com.fenix.dominio.servico.ContaService;
import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    ContaRepositorio contaRP; 
    
	public HomeController(ContaRepositorio contaRP) {
		super();
		this.contaRP = contaRP;
	}

	@ModelAttribute("totalconta")
	public double  TotalContas() {

		double total = 0.0; 
		
		if (UtilSerguranca.currentUser().isPresent()) 
		  	 total = contaRP.TotalConta(TipoConta.CC);
		System.out.println("Valor total das conta");
		System.out.println(total);
		return total;
	}
/*	
	@GetMapping("/teste")
	public ModelAndView teste() {

		return new ModelAndView("/teste/listar");
	}
*/	
	@GetMapping("/")
	public ModelAndView padrao() {
		System.out.println("passou padrao");
		return new ModelAndView("index","localDate",LocalDate.now()) ;				
	}
	@GetMapping("/home")
	public ModelAndView home() {
		System.out.println("passou home");
		return new ModelAndView("/error.html");
	}

		
}
