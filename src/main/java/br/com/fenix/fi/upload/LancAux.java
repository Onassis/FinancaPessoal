package br.com.fenix.fi.upload;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.abstrato.base.EntidadeAuditavelAuto;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.converter.rest.NumericBooleanDeserializer;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="LancAux" , indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
@Data
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class LancAux  extends EntidadeAuditavelAuto<UUID>  implements Comparable<LancAux> { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* detalhe do lancamento */ 
	private UUID  detalheLancId;
	
	/* detalhe do lancamento da conta destino - Transferencia */ 
	private UUID  detalheDestinoId;
	
	private UUID  lancamentoId;
	
	// ID do banco campo FITID do arquivo OFX
	private String chaveBanco; 	
	
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 
	
	
	@Column(length = 80)	
	private String informacao;
	
    private String observacao;	
    
	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dataDoc;
    
//    private Long  id;
	
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipoLancamento;
    
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoOperacao tipoOperacao;
    
   @JsonDeserialize(using = FavorecidoDeserializer.class)        
   @JsonBackReference
   @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)
    private Favorecido favorecido;
        
  
    @JsonDeserialize(using = ContaDeserializer.class)      
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = false)
    private Conta contaLancamento ;
    

    @JsonDeserialize(using = ContaDeserializer.class)      
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)
    private Conta contaDestino ;
    

    
    @JsonDeserialize(using = SubCategoriaDeserializer.class)  
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)	
    private SubCategoria subCategoria; 
    
 //   @JsonIgnore
 //   @ReadOnlyProperty
//    private Categoria lancamentoCategoria; 

//	@Column(columnDefinition = "DATE")	
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
//    private LocalDate dataVenc;
    
	/**
	 * Data da lançamento no banco 
	 */
	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dataLanc;
	
    @Column(nullable = true)
    private int nroPrestacao=1;
    
    @Column(nullable = true)
    private int nroInicialPrestacao=1 ;
    
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal total;
	
	
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal valor;
	
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal saldo;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;
	
	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal credito;

	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal debito;

 
	public LancAux() {
		super();
		this.valor = new BigDecimal(0); 
		this.total = new BigDecimal(0); 
		this.credito = new BigDecimal(0);
		this.debito = new BigDecimal(0);
		this.saldo = new BigDecimal(0); 
		
		this.nroInicialPrestacao = 1; 
		this.nroPrestacao = 1 ;
		this.conciliado = true;
	}
	
	public LancAux(Conta conta) {
		super();
		this.valor = new BigDecimal(0); 
		this.total = new BigDecimal(0); 
		this.credito = new BigDecimal(0);
		this.debito = new BigDecimal(0);
		this.saldo = new BigDecimal(0); 
		this.contaLancamento = conta;
		this.nroInicialPrestacao = 1; 
		this.nroPrestacao = 1 ; 
		this.conciliado = true;
	}
	 public String prestacao() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",nroInicialPrestacao);
		 sPrestacao = sPrestacao.concat("/");
		 sPrestacao = sPrestacao.concat(String.format("%02d",nroPrestacao));
		 return sPrestacao; 
	 }
	 public int getMes() {
		 return dataLanc.getMonthValue();
	 }
	 public int getAno() {
		 return dataLanc.getYear();
	 }
	 public boolean hasCriterio(String criterio) { 
		 return informacao.toUpperCase().contains(criterio.toUpperCase());
	 }
/*	public void setVencSubCategoria( SubCategoria subCategoria ) {
		this.lancamentoSubCategoria = subCategoria; 
		this.lancamentoCategoria = subCategoria.getCategoria();
	}
*/	
	
	 public void setLancamentoValor( BigDecimal valor) { 
	    	this.total = acertaSinal(valor); 
	    	this.valor = total.divide(new BigDecimal(this.nroPrestacao));    	
	 }
	 /*
	  * Acerta o sinal conforme se Credito e Debito 
	  * 
	  * Debito =>  Negativo
	  * Credito => Positivo 
	  */
	 public BigDecimal acertaSinal( BigDecimal valor) { 
		 if (isDebito()) { 
			 return valor.abs().multiply(new BigDecimal(-1)); 
		 }
		 return valor.abs();
	 }
	public boolean isDebito() {
		return this.tipoLancamento == TipoLancamento.D;
	}
	public boolean isCredito() {
		return this.tipoLancamento == TipoLancamento.C;
	}
	
	public boolean isContaCorrente() {
		if (contaLancamento == null) {
			return false; 
		}
		return contaLancamento.isContaCorrente();
	}
	public void setValor (BigDecimal valor) {
	    	this.valor = valor.abs();
	    	if (isDebito()) 
	    		this.valor = this.valor.negate() ; 
	    }
	
		
	 
	public BigDecimal getCredito() { 
		
		if (isCredito())
			return credito = this.valor.abs();
		
		return BigDecimal.ZERO; 		
	}
	public BigDecimal getDebito() { 
		
		if (isDebito()) 
			return debito = this.valor.abs();
		
		return BigDecimal.ZERO; 		
	}    
	 
	public BigDecimal getSaldoAnterior() { 
		return this.saldo.subtract(this.getCredito()).add(this.getDebito());
	}
	
	public BigDecimal acertaSaldo(BigDecimal saldoAnterior) {
		
		this.saldo = saldoAnterior.add(this.getCredito()).subtract(this.getDebito()); 
		return this.saldo; 		
	}
	public BigDecimal acertaSaldo() {
		
		return this.saldo.add(this.valor); 
				
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((detalheLancId == null) ? 0 : detalheLancId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LancAux other = (LancAux) obj;
		if (detalheLancId == null) {
			if (other.detalheLancId != null)
				return false;
		} else if (!detalheLancId.equals(other.detalheLancId))
			return false;
		return true;
	}

	@Override 
	public int compareTo(LancAux lancamentoDto) {

		return this.getId().compareTo(lancamentoDto.getId()); 		 
	}
    
	
	public boolean getConciliado() {
		if (this.detalheLancId == null) { 
			return false; 
		}
		return true ;	
	}

	
	public boolean isNovoLanc() { 
		if (this.detalheLancId == null)  
			return true; 
		
		return true  ;
	}

	public boolean isUpdateLanc() {
// Lancamento doJá tratado anteriomente 		
		if(conciliado)
			return false;
		
		if (this.detalheLancId == null)  
			return false; 
		
		return true  ;
	}
	public boolean isTransfLanc() { 
		if (this.detalheDestinoId == null)  
			return false; 
		
		return true ;
	} 

	

}
