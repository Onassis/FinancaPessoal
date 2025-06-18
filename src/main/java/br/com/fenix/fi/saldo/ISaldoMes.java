package br.com.fenix.fi.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

import br.com.fenix.fi.conta.Conta;

public interface ISaldoMes {

	    // Métodos que mapeiam diretamente para os aliases da query
	    Long getId();
	    Conta getContaId();
	    LocalDate getData();
	    Integer getAno();
	    Integer getMes();
	    
	    BigDecimal getSaldoInicial();
	    
	    
	    Boolean getFlagCompensacao();

	    BigDecimal getTotal(); // Vem da view_totalanomes


	    BigDecimal getSaldoAtual();
}
