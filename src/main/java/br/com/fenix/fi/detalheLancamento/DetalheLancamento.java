package br.com.fenix.fi.detalheLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.abstrato.base.EntidadeAuditavelAuto;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.fi.upload.LancAux;
import br.com.fenix.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity 
@Table(name="detalheLancamento", 
		indexes = { 
		@Index(name = "idx_dataPesquisa", columnList = "criado_por_id,dataRef", unique = false) }
)

@Data
@EqualsAndHashCode(callSuper =true)
@AllArgsConstructor
@ToString(callSuper =true)
@SuperBuilder
public class DetalheLancamento extends EntidadeAuditavelAuto<UUID> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6598853038445483479L;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
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
    private int prestacao;
    
	// ID do banco campo FITID do arquivo OFX
	private String chaveBanco; 	
	
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 

    @JsonDeserialize(using = ContaDeserializer.class)
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER  ,optional = true )
 	private Conta contaLancamento ;
   
    @JsonDeserialize(using = ContaDeserializer.class)
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER ,optional = true )
 	private Conta contaTransferencia ;
    /**
     * Valor da prestação, usado para calcular o total do lançamento
     **/
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal valor = BigDecimal.ZERO;
	
    /**
     * Valor efetivamento pago , usado para calcular o total do lançamento
     **/
	@Column(nullable = true, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal valorPgto ;
	
    @Column(nullable = false,columnDefinition = "DATE")	
    private LocalDate dataVenc;
    
	/**
	 * Data da lançamento no banco 
	 */
	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dataPgto;
	
	/**
	 * Data usada para pesquisa e Indice no banco de dados
	 */
	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dataRef;

    private boolean conciliado; 

    
	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal credito = BigDecimal.ZERO;;

	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal debito = BigDecimal.ZERO;;
	
	@Transient
	@Getter
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal valorLanc = BigDecimal.ZERO;
	
	public DetalheLancamento() {
		super();		
		valor = BigDecimal.ZERO;
		prestacao = 1 ; 
		conciliado = false; 	
		dataVenc = LocalDate.now();
		dataRef   = LocalDate.now();
	}

	
	public DetalheLancamento(LocalDate dataVenc, Conta conta,TipoLancamento tipoLancamento, BigDecimal valor) {
        super();		
        setDataVenc(dataVenc);
	
		this.tipoLancamento =  tipoLancamento; 
		this.contaLancamento = conta;
		this.valor = valor; 
		ajustarAntesSalvar();
		this.conciliado = false;
	}
	@PrePersist
	@PreUpdate
	public void ajustarAntesSalvar() {
		ajustarDataRef();
		ajustarValor();
		ajustarValorPgto();
	}
	
    public boolean isDebito() {
		return this.tipoLancamento == TipoLancamento.D;
	}
	public boolean isCredito() {
		return this.tipoLancamento == TipoLancamento.C;
	}
	/*
	 * Retorna o valor do lancamento 
	 */
	public BigDecimal getValorLanc() {		
		valorLanc = (conciliado) ? valorPgto : valor; 
		return Util.iniciaValor(valorLanc);
	}
	public BigDecimal getCredito() {
		return credito = (isCredito()) ? getValorLanc() : BigDecimal.ZERO; 		
	}
	public BigDecimal getDebito() {
		return debito = (isDebito()) ? getValorLanc().negate() : BigDecimal.ZERO; 		
	}
    public void setConciliado ( boolean conciliado) {
    	this.conciliado = conciliado; 
        if (!conciliado) {
        	dataPgto = null ;
        	valorPgto = BigDecimal.ZERO;
        }
    }

	public void ajustarDataRef() {
		
		dataRef = (conciliado) ? dataPgto : dataVenc; 
		dataRef = (dataRef == null) ? dataVenc : dataRef;
		
		this.ano =  dataRef.getYear();
		this.mes =  dataRef.getMonthValue() ;	
	}
	

	/**Método para atualizar a data o ano e o mes 
	 * @author Onassis tavares de souza
	 * @param dataVenc - Data 
	 * @return void  - 
	 */	
	public void setDataVenc(LocalDate dataVenc) {
		if (dataVenc == null) {
			return;
		}
		this.dataVenc = dataVenc;
		ajustarDataRef();
	}
	public void setDataPgto(LocalDate dataPgto) {
		this.dataPgto = dataPgto;
		ajustarDataRef();
		
	}
   public void setTipoLancamento ( TipoLancamento tipo) {
	   this.tipoLancamento = tipo; 
	   ajustarValor();
   }

/*
 * Grava valor negativo par lançamento Debito	
 */
	public void setValor(BigDecimal valor ) {
		this.valor = valor;
		ajustarValor();
	}
	/*
	 * Grava valor negativo par lançamento Debito	
	 */
	public void setValorPgto(BigDecimal valor ) {
			this.valorPgto = valor;
			ajustarValorPgto();
	}
	public void ajustarValorPgto() {
		    if (valorPgto == null) 
		    	valorPgto = BigDecimal.ZERO; 
		    
			this.valorPgto = valorPgto.abs();
			if (isDebito())  			
				this.valorPgto = this.valorPgto.negate() ; 
		}
	public void ajustarValor() {
		this.valor = valor.abs();
		if ( isDebito())  			
			this.valor = this.valor.negate() ; 
	}
	public boolean possuiContaLancamento() {
		return this.contaLancamento != null;  
	}
	public String getMesAnoLancamento() { 
		String mesAno; 
		if( dataVenc != null ) {
			mesAno = String.valueOf(dataVenc.getMonthValue()); 
			mesAno = mesAno + dataVenc.getYear(); 
			return mesAno;					
		}
		return null;
	}
	public boolean conciliarLancAux(LancAux lancAux) {
		
	   if (contaLancamento != null) {	
		   if  (!contaLancamento.equals(lancAux.getContaLancamento()))
			   return false; 
	   }
	   
	   if ( dataPgto.equals(lancAux.getDataLanc()) || chaveBanco.equals(lancAux.getChaveBanco())) 
	   		return true;		

	   if ( dataVenc.equals(lancAux.getDataLanc()) || valor.equals(lancAux.getValor()))  
	   		return true;	
	   
	   SubCategoria subCategoria = lancamento.getSubCategoria(); 
	   if ( subCategoria != null) {
		   if ( dataVenc.equals(lancAux.getDataLanc()) || subCategoria.equals(lancAux.getSubCategoria()))  
			   return true;
	   	}
	   return false; 
  }
}
