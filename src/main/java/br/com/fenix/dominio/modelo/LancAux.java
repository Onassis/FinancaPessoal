package br.com.fenix.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.converter.NumericBooleanDeserializer;
import br.com.fenix.dominio.converter.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.fi.conta.Conta;
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
import lombok.Data;
import lombok.ToString;


@Entity
@Table(name="LancAux" , indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
@Data
@ToString(callSuper = true)
public class LancAux  extends EntidadeAuditavel<Long>  implements Comparable<LancAux> { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long  detalheVencId;
	
	private Long  detalheDestinoId;
	
	private Long  lancamentoId;
	
	// ID do banco campo FITID do arquivo OFX
	private String chaveBanco; 	
	
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 
	
	
	@Column(length = 80)	
	private String lancamentoInformacao;
	
    private String lancamentoObservacao;	
    
	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate lancamentoDataDoc;
    
//    private Long  id;
	
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipoLancamento;
    
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoOperacao lancamentoTipoOperacao;
    
   @JsonDeserialize(using = FavorecidoDeserializer.class)        
   @JsonBackReference
   @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)
    private Favorecido lancamentoFavorecido;
        
  
    @JsonDeserialize(using = ContaDeserializer.class)      
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = false)
    private Conta contaLanc ;
    

    @JsonDeserialize(using = ContaDeserializer.class)      
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)
    private Conta contaDestino ;
    

    
    @JsonDeserialize(using = SubCategoriaDeserializer.class)  
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,  optional = true)	
    private SubCategoria lancamentoSubCategoria; 
    
 //   @JsonIgnore
 //   @ReadOnlyProperty
//    private Categoria lancamentoCategoria; 

	@Column(columnDefinition = "DATE")	
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dataVenc;
    
    @Column(nullable = true)
    private int lancamentoNroPrestacao=1;
    
    @Column(nullable = true)
    private int lancamentoNroInicialPrestacao=1 ;
    
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal lancamentoTotal;
	
	
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal valor;
	
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal saldo;

	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal credito;

	@Transient
	@JsonDeserialize(using = MoneyDeserializer.class) 	
	private BigDecimal debito;
	

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;
	
	
	
 
	public LancAux() {
		super();
		this.valor = new BigDecimal(0); 
		this.lancamentoTotal = new BigDecimal(0); 
		this.credito = new BigDecimal(0);
		this.debito = new BigDecimal(0);
		this.saldo = new BigDecimal(0); 
		
		this.lancamentoNroInicialPrestacao = 1; 
		this.lancamentoNroPrestacao = 1 ;
		this.conciliado = true;
	}
	
	public LancAux(Conta conta) {
		super();
		this.valor = new BigDecimal(0); 
		this.lancamentoTotal = new BigDecimal(0); 
		this.credito = new BigDecimal(0);
		this.debito = new BigDecimal(0);
		this.saldo = new BigDecimal(0); 
		this.contaLanc = conta;
		this.lancamentoNroInicialPrestacao = 1; 
		this.lancamentoNroPrestacao = 1 ; 
		this.conciliado = true;
	}
	 public String prestacao() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",lancamentoNroInicialPrestacao);
		 sPrestacao = sPrestacao.concat("/");
		 sPrestacao = sPrestacao.concat(String.format("%02d",lancamentoNroPrestacao));
		 return sPrestacao; 
	 }
	 
	 public boolean criterio(String criterio) { 
		 return lancamentoInformacao.toUpperCase().contains(criterio.toUpperCase());
	 }
/*	public void setVencSubCategoria( SubCategoria subCategoria ) {
		this.lancamentoSubCategoria = subCategoria; 
		this.lancamentoCategoria = subCategoria.getCategoria();
	}
*/	
	
	 public void setLancamentoValor( BigDecimal valor) { 
	    	this.lancamentoTotal = acertaSinal(valor); 
	    	this.valor = lancamentoTotal.divide(new BigDecimal(lancamentoNroPrestacao));    	
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
		if (contaLanc == null) {
			return false; 
		}
		return contaLanc.isContaCorrente();
	}
	public void setValor (BigDecimal valor) {
	    	this.valor = valor.abs();
	    	if (isDebito()) 
	    		this.valor = this.valor.negate() ; 
	    }
	
		
	 
	public BigDecimal getCredito() { 
		
		if (isCredito())
			return credito = this.valor;
		
		return BigDecimal.ZERO; 		
	}
	public BigDecimal getDebito() { 
		
		if (isDebito()) 
			return debito = this.valor.multiply( new BigDecimal(-1)); 
		
		return BigDecimal.ZERO; 		
	}    
	 
	public BigDecimal getSaldoAnterior() { 
		return this.saldo.subtract(getValor() );
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
		result = prime * result + ((detalheVencId == null) ? 0 : detalheVencId.hashCode());
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
		if (detalheVencId == null) {
			if (other.detalheVencId != null)
				return false;
		} else if (!detalheVencId.equals(other.detalheVencId))
			return false;
		return true;
	}

	@Override 
	public int compareTo(LancAux lancamentoDto) {

		return (int) (this.getId() -  lancamentoDto.getId()); 		 
	}
    
	
	public boolean getConciliado() {
		if (this.detalheVencId == null) { 
			return false; 
		}
		return ( this.detalheVencId != 0)  ;	
	}
	
/*	@Transient 
	public boolean isConciliado() {
		System.out.println("Conciliado " + this.detalheVencId ) ; 
		return ( this.detalheVencId != 0)  ;
	} 
*/
}
