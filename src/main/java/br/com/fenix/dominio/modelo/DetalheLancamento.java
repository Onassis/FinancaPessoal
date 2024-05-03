package br.com.fenix.dominio.modelo;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity 
@Table(name="detalheLancamento", 
		indexes = { 
		@Index(name = "idx_dataPesquisa", columnList = "criado_por_id,dataVenc", unique = false) ,
		@Index(name = "idx_AnoMes", columnList = "criado_por_id,Ano,Mes,conta_lancamento_id", unique = false) })
@EqualsAndHashCode(callSuper=true)
@Data
@ToString(callSuper =true)
public class DetalheLancamento extends EntidadeAuditavel<Long> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6598853038445483479L;
	
	@ManyToOne(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name="lancamento_id",  nullable = false, updatable = false)
// Evita o erro N+1	
	@JsonBackReference
	private Lancamento lancamento ;   
	
	@Column(nullable = false)
	private int ano;
	@Column(nullable = false)
	private int mes;
	
	
	@Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;	
	   
    @Column(nullable = true)
    private int prestacao=1;
    
	// ID do banco campo FITID do arquivo OFX
	private String chaveBanco; 	
	
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 

    @JsonDeserialize(using = ContaDeserializer.class)
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER ,optional = true )
 	private Conta contaLancamento ;
   
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal valor = BigDecimal.ZERO;
	
	
    @Column(nullable = false,columnDefinition = "DATE")	
    private LocalDate dataVenc;
    
    /** 
     * Data usada com referencia do mes e ano e também nos Select de pesquisas  
     * É usada a data venc. quando a data de compensação estiver vazia 
     */

    
    
    private boolean conciliado= false; 

    
	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal credito;

	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal debito;
	
    
	public DetalheLancamento() {
		super();
		valor = BigDecimal.ZERO;
	}
	
	public DetalheLancamento(LocalDate dataVenc, Conta conta,TipoLancamento tipoLancamento, BigDecimal valor) {
        super();		
        setDataVenc(dataVenc);
	
		this.tipoLancamento =  tipoLancamento; 
		this.contaLancamento = conta;
		this.valor = valor; 
		ajustaValor();
	}

	public boolean isDebito() {
		return this.tipoLancamento == TipoLancamento.D;
	}
	public boolean isCredito() {
		return this.tipoLancamento == TipoLancamento.C;
	}
	public BigDecimal getCredito() { 		
		if (isCredito())
			return credito = this.valor;		
		return BigDecimal.ZERO; 		
	}
	public BigDecimal getDebito() { 		
		if (isDebito()) 
			return debito = this.valor.abs().negate(); 		
		return BigDecimal.ZERO; 		
	}


	/**Método para atualizar a data o ano e o mes 
	 * @author Onassis tavares de souza
	 * @param dataVenc - Data 
	 * @return void  - 
	 */	
	public void setDataVenc(LocalDate dataVenc) {
		if (dataVenc != null) {	
			this.ano =  dataVenc.getYear();
			this.mes =  dataVenc.getMonthValue() ;			
		}
		this.dataVenc = dataVenc;
	}

   public void setTipoLancamento ( TipoLancamento tipo) {
	   this.tipoLancamento = tipo; 
	   ajustaValor();
   }

/*
 * Grava valor negativo par lançamento Debito	
 */
	public void setValor(BigDecimal valor ) {
		this.valor = valor;
		ajustaValor();
	}
	
	
	public void ajustaValor() {
		this.valor = valor.abs();
		if ( isDebito())  			
			this.valor = this.valor.negate() ; 
	}
	public boolean possuiContaLancamento() {
		return this.contaLancamento != null;  
	}
}
