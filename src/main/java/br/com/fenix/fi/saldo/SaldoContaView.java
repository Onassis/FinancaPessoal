package br.com.fenix.fi.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.PersistenceCreator;

import br.com.fenix.fi.conta.Conta;
import lombok.Getter;
import lombok.Setter;

// Um record é uma classe final, e seus campos são 'private final' por padrão.
// O construtor, getters (sem "get"), equals, hashCode e toString são gerados automaticamente.
@Getter
@Setter
public class SaldoContaView{ 
	
	
	private  Long id;
//    Boolean flagCompensacao,
	private BigDecimal saldoInicial;
//    BigDecimal total, // Vem da view_totalanomes
//     private BigDecimal saldoAtual;
     
	public SaldoContaView() {}
     
     @PersistenceCreator
     public SaldoContaView(Long id, BigDecimal saldoInicial) {
		super();
		this.id = id;
		this.saldoInicial = saldoInicial;
	}
}
