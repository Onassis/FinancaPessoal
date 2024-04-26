package br.com.fenix.dominio.servico;

import com.google.common.collect.Iterables;
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
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.SaldoConta;
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
	SaldoServico saldoSC;
	
	@Autowired
	AutomacaoServico autoSC;
	
	@Autowired
	LancamentoServico  lancSC;
	
	@Autowired
	LancamentoRepositorio  lancRP;
	@Autowired
	DetalheLancamentoRepositorio  detLancRP;
	
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

	@Transactional
	public void  gerarLancamento(ArrayList<LancAux> dados) {   
		
		ArrayList<Lancamento> lancamentos = new ArrayList<>(); 

		LancAux lanc = dados.get(0);
		Conta conta = lanc.getContaLanc();
				
//---------------- Salva Lançamento --------------------------------------------------// 
		for (LancAux lancAux  : dados   ) {
			LancamentoDTO lancDTO = modelMapper.map(lancAux, LancamentoDTO.class);	
			lancDTO.setConciliado(true);
			lancDTO.setContaLancamento(conta);
/*			if (!saldo.isSaldoAtual(lancDTO.getDataVenc())) { 
				saldo = saldoSC.buscaSaldo(lancDTO.getContaLancamento(), lancDTO.getDataVenc());
				saldos.add(saldo);
			}	
			if (lancDTO.getContaDestino() != null ) {
				SaldoConta saldoDestino = saldoSC.buscaSaldo(lancDTO.getContaDestino(), lancDTO.getDataVenc());
				if (!saldoDestinos.contains(saldoDestino)) {
					saldoDestinos.add(saldoDestino);
				}
				
			}
*/			
			lancamentos.add(lancSC.criar(lancDTO));
		}
		
		lancRP.saveAll(lancamentos);
		
		
		
		saldoRP.f_atualiza_saldo(lanc.getCriadoPor().getId(), lanc.getContaLanc().getId(),
				lanc.getDataVenc(), lanc.getSaldoAnterior());
		
		/* -------------------------------------------------------- 
		 *     Acerta Saldo 
		 * -------------------------------------------------------- */
/*		BigDecimal saldoFinal  = Iterables.getLast(dados).getSaldo();
		LocalDate dataUltLanc  = Iterables.getLast(dados).getDataVenc();
		
		for (int i = saldos.size() -1 ; i  >= 0 ; i--)  {
			SaldoConta saldoAux  = saldos.get(i);
			saldoAux.setFlag_compensacao(true);
			if (saldoAux.isSaldoAtual(dataUltLanc)) {
				LocalDate dataInicio = LocalDate.of(dataUltLanc.getYear(), dataUltLanc.getMonthValue(), 1);
				double totalMes = detLancRP.TotalMesConta(saldoAux.getConta(), dataInicio, dataUltLanc);
				saldoFinal = BigDecimal.valueOf(totalMes).subtract(saldoFinal);
				saldoAux.setSaldoInicial(saldoFinal);			
				
			} else { 				
				double totalMes = detLancRP.TotalMesConta(saldoAux.getConta(), saldoAux.getAno(), saldoAux.getMes());
				saldoFinal = BigDecimal.valueOf(totalMes).subtract(saldoFinal);
				saldoAux.setSaldoInicial(saldoFinal);
			}	
		}	

		saldoRP.saveAll(saldos);
		saldoRP.saveAll(saldoDestinos);
*/		
	}				
	public Coletor  processaCSV(Conta conta,List<String> conteudo) {

		Coletor coletorLanc  = new Coletor();

//---------------- Cria Lançamentos --------------------------------------------------// 
		
		for (String textoLinha  : conteudo) {
		       System.out.println(textoLinha);
	    		 coletorLanc.AddLancamentoCSV( conta, textoLinha );
  
		}
		
		//---------------- Ajusta os Lançamentos -------------------------------------------------- 		
		for (LancAux lancDTO  : coletorLanc.getLancamentosAux()  ) {
					autoSC.automatizar(lancDTO);
 		}
			
		return coletorLanc;
	}	
	
	public Coletor  processaOFX(Conta conta,List<String> conteudo) {

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
	
		BigDecimal saldoAnt = coletorLanc.UltimoLancamento().getSaldoAnterior();
		for (int i = coletorLanc.getLancamentosAux().size() -2 ; i  >= 0 ; i--)  {			
			LancAux   lancAux = coletorLanc.getLancamentosAux().get(i);
			 lancAux.setSaldo(saldoAnt);
			 saldoAnt = lancAux.getSaldoAnterior();  			 			
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
			LancAux   lancAux = coletorLanc.getLancamentosAux().get(i);
			      lancAux.setSaldo(saldo); 
		          saldo = lancAux.acertaSaldo(); 				 			
		}
		return coletorLanc;
	}
	
}
