package br.com.fenix.fi.saldo;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.seguranca.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="saldo_conta",
		indexes = { @Index(name = "idx_saldo", columnList = "criado_por_id,conta_id,data", unique = true)})				   
public class SaldoConta extends EntidadeAuditavel<Long> {

	
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,optional = false )
 	private Conta conta ;

    @Column(columnDefinition = "DATE")	
    private LocalDate data;
    

	@Column(columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal saldoInicial;
	
	@Column(nullable = false)
	private int ano;

	@Column(nullable = false)
	private int mes;
	
	private boolean flag_compensacao = false; 
	

	public void setData (LocalDate data) {
        this.data = data;		
        this.ano = data.getYear();
        this.mes = data.getMonthValue();        
	}


	public SaldoConta(Conta conta, LocalDate dataSaldo, BigDecimal saldoAnterior) {		
		super();
		this.conta = conta; 
		this.setData(dataSaldo);
		this.saldoInicial = saldoAnterior;
	}

	public SaldoConta(Conta conta, LocalDate data) {
		
		super(conta.getCriadoPor());
		saldoInicial = BigDecimal.ZERO;
		this.conta = conta;
		setData(data);
	}
	public SaldoConta() {
		super();
	}


	public boolean isAnoMesCorrente() {
		return (ano == LocalDate.now().getYear() && mes == LocalDate.now().getMonthValue());
	}
	public boolean isSaldoAtual(LocalDate dataLanc) {
		return (this.ano == dataLanc.getYear() && this.mes == dataLanc.getMonthValue());
	}
	public boolean isFuturo() {
		return data.isAfter(LocalDate.now());
	}
}
