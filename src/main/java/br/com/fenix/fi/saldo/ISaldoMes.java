package br.com.fenix.fi.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

public interface ISaldoMes {

	    // Métodos que mapeiam diretamente para os aliases da query
	    Long getId();
	    Long getContaId();
	    Integer getAno();
	    Integer getMes();
	    LocalDate getData();
	    Boolean getFlagCompensacao();
	    BigDecimal getSaldoInicial();
	    BigDecimal getTotal(); // Vem da view_totalanomes

	    // Para campos calculados, usamos @Value com SpEL (Spring Expression Language)
	    // 'target' se refere ao objeto de resultado bruto antes da projeção
	    @Value("#{target.saldoInicial + (target.total == null ? 0 : target.total)}")
	    BigDecimal getSaldoAtual();
}
