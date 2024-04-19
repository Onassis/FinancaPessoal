package br.com.fenix.dominio.servico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.SaldoConta;
import br.com.fenix.dominio.repositorio.DetalheLancamentoRepositorio;

import br.com.fenix.dominio.repositorio.dadosBasico.SaldoContaRepositorio;
import br.com.fenix.dominio.view.ITotalMesDetalhe;

import br.com.fenix.seguranca.modelo.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;

@Service
public class SaldoServico {
	
	@Autowired
	SaldoContaRepositorio saldoRP; 
	
	@Autowired
	DetalheLancamentoRepositorio detRP; 
	
	
	public  List<SaldoConta> atualizaSaldo ( Conta conta, LocalDate data, BigDecimal valor ) {
		
		List<SaldoConta> saldos = new ArrayList<>() ; 
		
		LocalDate dataSaldo = data;  
		double totalLanc =0; 
		
		// Não gera saldo par futuro 
		if(data.isAfter(LocalDate.now())){
			return saldos;
		}
		
		
		SaldoConta saldoLanc = buscaSaldo (conta,data);
		saldos.add(saldoLanc);
		
		if(saldoLanc.isAnoMesCorrente()) {
			return saldos;
		}
		
// Atualiza saldo com lancamento 
		
		dataSaldo = saldoLanc.getData();
		
		List<SaldoConta> saldoContas = saldoRP.findByContaAndGtData(conta, dataSaldo); 
		
		List<ITotalMesDetalhe> totalMesDets = detRP.findByContaGeData(conta.getId(), dataSaldo); 
		
		BigDecimal saldoIni = saldoLanc.getSaldoInicial() ;
		
		for ( ITotalMesDetalhe totalMesDet : totalMesDets ) {
			System.out.println("Conta " +  totalMesDet.getConta() + " data" + totalMesDet.getData() );
			data.plusMonths(1);
            if ( data.isBefore(LocalDate.now())) {  		
            	SaldoConta saldo  = saldoContas.stream()
					  .filter(saldoConta -> saldoConta.getData().equals(data))
					  .findAny()
					  .orElse(new SaldoConta( conta, data) );
            	saldo.setSaldoInicial(saldoIni.add( totalMesDet.getValor()));
            	saldos.add(saldo);
            }
		}
		
		/*		
		if ( conta.getTipoConta() == TipoConta.CR ) {		
			if ( data.getDayOfMonth() -  conta.getDiaVencimento() > 9) {
				data.plusMonths(1);
				dataSaldo = LocalDate.of(data.getYear(), data.getMonth(), conta.getDiaVencimento());
			}
			else 
				
			dataSaldo = LocalDate.of(data.getYear(), data.getMonth(), conta.getDiaVencimento()); 
			
		}
*/
/*		
		for ( SaldoConta saldo : saldoContas ) {
		
			 
		 totalLanc = detRP.TotalConta(saldo.getConta(), saldo.getData());
			 
			 TotalMesDetalheView total  = totalMesDets.stream()
					  .filter(totalMes -> totalMes.getData().equals(data))
					  .findAny()
					  .orElse(new TotalMesDetalheView(saldoLanc.getConta()));
					  
			 saldoIni = saldo.getSaldoInicial().add( total.getValor()); 
			 saldo.setSaldoInicial( saldo.getSaldoInicial().add(saldoIni)); 
		}
		
		saldoContas.add(saldoLanc);
*/		
		return saldos;
		
	}
	
	public SaldoConta buscaSaldoAtualAnterior ( Conta conta, LocalDate data   )  { 
					
		Usuario usuario = UtilSerguranca.currentUser().get();
	    Optional<SaldoConta> saldoAnterior = saldoRP
	    					.findByContaDataSaldoAnterior(usuario.getId(),
	    							conta.getId(),
	    							data ) ;
		if (!saldoAnterior.isEmpty()) {
				return saldoAnterior.get();	
		}
		return null;	
	}
	public SaldoConta buscaSaldo ( Conta conta, LocalDate data   ) {
		
// Retorna saldo do dia 		
//
		LocalDate datSaldo = conta.getDataSaldo(data) ; 
		
		Optional<SaldoConta> saldoOp = saldoRP.findByContaAndData(conta,datSaldo ) ;
		
		
		if (!saldoOp.isEmpty()) {
			return saldoOp.get(); 
		}
// Cria saldo do dia baseado no saldo Anterior 
		
		Usuario usuario = UtilSerguranca.currentUser().get();
	    Optional<SaldoConta> saldoAntOp = saldoRP
		    					.findByContaDataSaldoAnterior(usuario.getId(),
		    							conta.getId(),
		    							datSaldo ) ;
	    if (!saldoAntOp.isEmpty()) {
			 SaldoConta saldoAt = saldoAntOp.get();
			 SaldoConta saldo = new SaldoConta();
			 saldo.setData(datSaldo);
			 saldo.setConta(conta);  
			 double totalLanc = detRP.TotalConta(conta, saldoAt.getData());
			 
			 saldo.setSaldoInicial( saldoAt.getSaldoInicial().add( new BigDecimal(totalLanc))); 
			 
//			 saldo.setSaldo(saldoAt.getSaldo());
			 return saldo; 
	    }
// Cria saldo do dia primeiro lançamento  
	    
		 SaldoConta saldo = new SaldoConta();
		 saldo.setData(datSaldo);
		 saldo.setConta(conta);
//		 saldo.setSaldo(conta.getSaldo());
		 saldo.setSaldoInicial(conta.getSaldo());
		 return saldo; 
	}
	/*
	public SaldoConta buscaSaldoDestino ( LancamentoDTO lancDTO ) {
		SaldoConta saldo; 
		
		Optional<SaldoConta> saldoOp = saldoRP.findByContaAndData(lancDTO.getContaDestino() ,
				lancDTO.getDataVenc()) ;
		if (saldoOp.isEmpty()) {
			 saldo = new SaldoConta();
			 saldo.setData(lancDTO.getDataVenc());
			 saldo.setConta(lancDTO.getContaDestino());
	
			 saldo.setSaldoInicial(lancDTO.getSaldo());
		}
		else {
			saldo = saldoOp.get();
		}
		return saldo;
	}
	*/
}
