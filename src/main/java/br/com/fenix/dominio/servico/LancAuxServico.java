package br.com.fenix.dominio.servico;

import java.io.DataInput;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.SaldoConta;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.repositorio.DetalheLancamentoRepositorio;
import br.com.fenix.dominio.repositorio.LancAuxRepositorio;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SaldoContaRepositorio;
import br.com.fenix.util.Coletor;
import br.com.fenix.util.Tag;
import groovy.cli.Option;
import org.modelmapper.*;

@Service
public class LancAuxServico {

	@Autowired
	LancAuxRepositorio lancAuxRP;
	
	@Autowired
	SaldoContaRepositorio saldoRP;
	
	
	@Autowired
	AutomacaoServico autoSC;
	
	@Autowired
	LancamentoServico  lancSC;
	
	@Autowired
	LancamentoRepositorio  lancRP;

	@Autowired
	private ModelMapper modelMapper;
	

	@Transactional
	public Iterable<LancAux>  excluiSalvaTodos(List<LancAux> lancamentosAux) {
		System.out.println("excluir Salva Lancamento DTO");
		lancAuxRP.deleteAll();
		return lancAuxRP.saveAll(lancamentosAux);	 
	}

	
	private LancAux ultimoLancamento(ArrayList<LancAux> lancamentosAux) {
	      if (lancamentosAux.size() == 0) {
	            return null;
	        } else {
	            return lancamentosAux.get(lancamentosAux.size() - 1);
	        }
	}
	private SaldoConta ultimoSaldo(ArrayList<SaldoConta> ListaSaldo) {
	      if (ListaSaldo.size() == 0) {
	            return null;
	        } else {
	            return ListaSaldo.get(ListaSaldo.size() - 1);
	        }
	}
	
/*	
	@Transactional
	public void  gerarLancamento(ArrayList<LancamentoDTO> dados) {
		
		SaldoConta saldo;
		
		ArrayList<SaldoConta> listSaldos = new ArrayList<>(); 
		
		LocalDate  dataSaldo; 
		
		System.out.println("gera lancamento ");
		
	}
	
*/
	
	public void  gerarLancamento(ArrayList<LancAux> dados) {   
		
		ArrayList<Lancamento> lancamentos = new ArrayList<>(); 
		
		BigDecimal saldo = dados.get(dados.size()-1).getSaldo() ;
		LancAux lanc = dados.get(0);
		Conta conta = lanc.getContaLanc();
//---------------- Salva Lançamento --------------------------------------------------// 
		for (LancAux lancAux  : dados   ) {
			LancamentoDTO lancDTO = modelMapper.map(lancAux, LancamentoDTO.class);				
			lancDTO.setContaLancamento(conta);
			lancamentos.add(lancSC.criar(lancDTO));
		}
		lancRP.saveAll(lancamentos);
	}				
	
	public Coletor  processInput(Conta conta,List<String> conteudo) {

		Coletor coletorLanc  = new Coletor();

//---------------- Cria Lançamentos --------------------------------------------------// 
		
		for (String textoLinha  : conteudo) {
	         System.out.println(textoLinha);
	         Tag tag = new Tag (textoLinha);	
	         System.out.println(tag);
	         coletorLanc.AddLLancamento(conta, tag);
		}
//---------------- Ajusta os Lançamentos -------------------------------------------------- 
		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			autoSC.automatizar(lancDTO);
		}
		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			lancSC.conciliar(lancDTO);
		}

//---------------- Acerta Saldo do LançamentoDTO --------------------------------------------------// 
	
		BigDecimal saldo = coletorLanc.UltimoLancamento().getSaldo();
		for (int i = coletorLanc.getLancamentosAux().size() -2 ; i  >= 0 ; i--)  {
			LancAux   lancAux = coletorLanc.getLancamentosAux().get(i);
					lancAux.setSaldo(saldo); 
		      		saldo = lancAux.acertaSaldo(); 			 			
		}
		return coletorLanc;
	}
	public Coletor  processInputCSV(Conta conta,List<String> conteudo) {

		Coletor coletorLanc  = new Coletor();

//---------------- Cria Lançamentos --------------------------------------------------// 
		
		for (String textoLinha  : conteudo) {
	         System.out.println(textoLinha);
	         Tag tag = new Tag (textoLinha);	
	         System.out.println(tag);
	         coletorLanc.AddLLancamento(conta, tag);
		}
//---------------- Ajusta os Lançamentos --------------------------------------------------// 
		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			autoSC.automatizar(lancDTO);
		}
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
			lancSC.conciliar(lancDTO);
		}
//---------------- Acerta Saldo do LançamentoDTO --------------------------------------------------// 
		
		BigDecimal saldo = coletorLanc.UltimoLancamento().acertaSaldo();
		for (int i = coletorLanc.getLancamentosAux().size() -2 ; i  >= 0 ; i--)  {
			LancAux   lancDTO = coletorLanc.getLancamentosAux().get(i);
			      lancDTO.setSaldo(saldo); 
		          saldo = lancDTO.acertaSaldo(); 				 			
		}
		return coletorLanc;
	}
	
}
