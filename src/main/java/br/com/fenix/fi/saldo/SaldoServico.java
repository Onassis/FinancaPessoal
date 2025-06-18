package br.com.fenix.fi.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hpsf.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.view.ITotalMesDetalhe;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamentoRepositorio;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.seguranca.util.UtilSerguranca;

@Service
public class SaldoServico {
	
	@Autowired
	SaldoContaRepositorio saldoRP; 
	
	@Autowired
	DetalheLancamentoRepositorio detRP; 
	
	@Transactional(propagation = Propagation.REQUIRED) 
	public void atualizaSaldo ( Conta conta, LocalDate data, BigDecimal valor ) {
		
		/* Verifica se a data é futura. Se for, não atualiza o saldo. */
		if(conta.isContaCorrente() && data.isAfter(LocalDate.now())) {
			return;
		}
		/* Data do Saldo mensal              LocalDate.of(data.getYear(), data.getMonth(), 1); */ 
		LocalDate dataSaldo = conta.dataSaldoAnterior(data);
				 
		
		Optional<SaldoConta> saldoMes = saldoRP.findByContaAndData(conta,dataSaldo);
    	
		if (saldoMes.isPresent()) {
			saldoRP.atualizaContaGeDataSaldo(conta.getId(), dataSaldo, valor);       
			return; 
		}
		
		BigDecimal saldoAnterior =  buscarSaldoFinalDiaAnterior(conta,dataSaldo); 
		
		SaldoConta  saldo = new SaldoConta(conta,dataSaldo,saldoAnterior) ;
		saldoRP.save(saldo);
		saldoRP.atualizaContaGeDataSaldo(conta.getId(), dataSaldo, valor);       
	}	
	
    /**
     * Encontra o valor do saldo final do dia anterior à data fornecida.
     */
    private BigDecimal buscarSaldoFinalDiaAnterior(Conta conta, LocalDate data) {
    	
    	// Busca a data anterior do ultimo saldo registrado para a conta e data especificada.
    	
    	Optional<LocalDate> dataAnterior =  saldoRP.findUltimaDataSaldo(conta, data); 
    	
//
    	if (dataAnterior.isEmpty()) {
			// Se não encontrou nenhum saldo anterior, retorna o saldo inicial da conta.
			return conta.getSaldo();
    	}
//    	List<SaldoContaView>  saldoMesAnterior = saldoRP.findSaldoMesByContaByData(conta.getId(), dataAnterior.get());
    	Optional< ISaldoMes> saldoMesAnterior = saldoRP.findSaldoMesByContaByData(conta.getId(), dataAnterior.get());
    	if (saldoMesAnterior.isPresent()) {
//    		// Se encontrou um saldo anterior, retorna o saldo inicial desse registro.
    		return saldoMesAnterior.get().getSaldoAtual();
//    	
    	}
    	
       return conta.getSaldo();
//        
    }
//	public SaldoConta buscaSaldoAtualAnterior ( Conta conta, LocalDate data   )  { 
//		
//		Usuario usuario = UtilSerguranca.currentUser().get();
//	    Optional<SaldoConta> saldoAnterior = saldoRP
//	    					.findByContaDataSaldoAnterior(usuario.getId(),
//	    							conta.getId(),
//	    							data ) ;
//		if (!saldoAnterior.isEmpty()) {
//				return saldoAnterior.get();	
//		}
//		return null;	
//	}
//	public SaldoConta buscaSaldo ( Conta conta, LocalDate data   ) {
//		
//// Retorna saldo do dia 		
////
//// Cria saldo do dia baseado no saldo Anterior 
//		
//		Usuario usuario = UtilSerguranca.currentUser().get();
//	    Optional<SaldoConta> saldoAntOp = saldoRP
//		    					.findByContaDataSaldoAnterior(usuario.getId(),
//		    							conta.getId(),
//		    							data ) ;
//       return null; 
//	}
    
//	  /**
//     * Recalcula o saldo para uma data específica e propaga a atualização para os dias subsequentes.
//     * Este é o método principal a ser chamado pelo listener.
//     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW) // Roda em uma nova transação
//    public void recalcularSaldosAPartirDe(Conta conta,  LocalDate dataInicial) {
//    	
////    	List<SaldoMes> saldoMes = saldoMesRP.findByContaIdAndData(conta.getId(),dataInicial); 
////    	List<SaldoMes> saldoMes = saldoMesRP.findAll(); 
////    	saldoMes.stream().forEach(System.out::println); 
//    	
//    	
    	
//        Conta conta = contaRepository.findById(contaId)
//                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + contaId));
//
//        // 1. Calcular o saldo para a data do lançamento que foi alterado
//        BigDecimal saldoFinalDiaAnterior = calcularSaldoParaDia(conta, dataInicial);
//
//        // 2. Propagar a mudança para os dias seguintes que já possuem registro de saldo
//        List<Saldo> saldosSubsequentes = saldoRepository.findByContaAndDataGreaterThanOrderByDataAsc(conta, dataInicial);
//
//        for (Saldo saldoDiaSeguinte : saldosSubsequentes) {
//            // O saldo anterior do dia seguinte é o saldo final do dia que acabamos de calcular
//            saldoDiaSeguinte.setSaldoAnterior(saldoFinalDiaAnterior);
//            saldoRepository.save(saldoDiaSeguinte);
//
//            // Atualiza o saldo final para a próxima iteração
//            saldoFinalDiaAnterior = saldoDiaSeguinte.getSaldoFinalDoDia();
//        }
//    }
    

//		List<SaldoConta> saldos = new ArrayList<>() ; 
		
//		LocalDate dataSaldo = data;  
//		double totalLanc =0; 
		
//		// Não gera saldo par futuro 
//		if(data.isAfter(LocalDate.now())){
//			return saldos;
//		}
//		
//		
//		SaldoConta saldoLanc = buscaSaldo (conta,data);
//		saldos.add(saldoLanc);
//		
//		if(saldoLanc.isAnoMesCorrente()) {
//			return saldos;
//		}
//		
//// Atualiza saldo com lancamento 
//		
//		dataSaldo = saldoLanc.getData();
//		
//		List<SaldoConta> saldoContas = saldoRP.findByContaAndGtData(conta, dataSaldo); 
//		
//		List<ITotalMesDetalhe> totalMesDets = detRP.findByContaGeData(conta.getId(), dataSaldo); 
//		
//		BigDecimal saldoIni = saldoLanc.getSaldoInicial() ;
//		
//		for ( ITotalMesDetalhe totalMesDet : totalMesDets ) {
//			System.out.println("Conta " +  totalMesDet.getConta() + " data" + totalMesDet.getData() );
//			data.plusMonths(1);
//            if ( data.isBefore(LocalDate.now())) {  		
//            	SaldoConta saldo  = saldoContas.stream()
//					  .filter(saldoConta -> saldoConta.getData().equals(data))
//					  .findAny()
//					  .orElse(new SaldoConta( conta, data) );
//            	saldo.setSaldoInicial(saldoIni.add( totalMesDet.getValor()));
//            	saldos.add(saldo);
//            }
//		}
		
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
//		return saldos;
		
//	}

//    private BigDecimal calcularSaldoParaDia(Conta conta, LocalDate data) {
// 
//    	// Passo 1: Buscar o saldo do dia anterior
//        BigDecimal saldoAnteriorValor = buscarSaldoFinalDiaAnterior(conta, data);
//
//        // Passo 2: Calcular o total de lançamentos do dia
//        BigDecimal totalLancamentosDia = BigDecimal.ZERO;
////        		detRP.sumTotalValorByContaAndData(conta, data);
//
//        // Passo 3: Criar ou atualizar o registro de Saldo para o dia atual
//        SaldoConta saldoDoDia = saldoRP.findByContaAndData(conta, data) 
//                .orElse(new SaldoConta()); // Cria um novo se não existir
//
//        saldoDoDia.setConta(conta);
//        saldoDoDia.setData(data);
////        saldoDoDia.setSaldoAnterior(saldoAnteriorValor);
////        saldoDoDia.setTotalLancamento(totalLancamentosDia);
//
//        saldoRP.save(saldoDoDia);
//
//  //      return saldoDoDia.getSaldoFinalDoDia();
//        return BigDecimal.ZERO;
//    }
//
	
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
