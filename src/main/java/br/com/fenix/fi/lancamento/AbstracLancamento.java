package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)

public abstract class AbstracLancamento<ID> extends EntidadeAbstrata<ID> {
	  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataDoc;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataVenc;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataPgto;
    
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataRef;
    
    protected TipoLancamento tipoLancamento;
	
	@JsonDeserialize(using = FavorecidoDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)	
    protected Favorecido favorecido;
	
    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected Conta contaLancamento ;
    
 // chave de lancamento da conta 
    protected String chaveBanco;
    
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
    protected String refBanco; 

    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)
    protected Conta contaTransferencia ;

    @JsonDeserialize(using = SubCategoriaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected SubCategoria subCategoria;
      
    protected int nroPrestacao=1;
    protected int nroInicialPrestacao=1 ;
    
    /**
     * Prestação atual do parcelamento
     */
    protected int prestacao;

    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal total = BigDecimal.ZERO;
    
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal valor = BigDecimal.ZERO;
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal valorPgto  = BigDecimal.ZERO;
    
    
    @Getter
    protected BigDecimal valorLanc  = BigDecimal.ZERO; 
    
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal saldo  = BigDecimal.ZERO;
    @Transient
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal credito  = BigDecimal.ZERO;
    @JsonDeserialize(using = MoneyDeserializer.class) 
    @Transient
	protected BigDecimal debito  = BigDecimal.ZERO;
    

   
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;
    
    /* Entrada em emprestimo */ 
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
   	protected boolean entrada ;
    
    
//	private String prestacaoAtual;
//	  
    public AbstracLancamento() {    	
    	
    	this.dataDoc = LocalDate.now();
    	this.dataVenc = LocalDate.now();
//    	this.valor = BigDecimal.ZERO; 
//    	this.total = BigDecimal.ZERO;
//    	this.credito = BigDecimal.ZERO; 
//    	this.debito = BigDecimal.ZERO; 
//        this.nroPrestacao = 1;
//        this.nroInicialPrestacao = 1;
    	
    }


   
    /*
     * Ao atualizar Data Doc atualiza data Vencimento
     */
    
//    public void setDataDoc(LocalDate data) {
//    	this.dataDoc = data;
//    	if (tipoOperacao != TipoOperacao.CP) {
//        	this.dataVenc = data;     		
//        	ajustaDataRef();    		
//    	}
//    }
    public void setDataPgto(LocalDate dataPgto) {
    	this.dataPgto = dataPgto; 
    	ajustaDataRef();
    }
    protected void ajustaDataRef() {
		if (conciliado) {
	    	this.dataRef = dataPgto;
		}
		else 
			this.dataRef = this.dataVenc;
		
	}

	public void setLancamentoTotal( BigDecimal total) { 
    	this.total = acertarSinal(total); 
    	this.valor = total.divide(new BigDecimal(nroPrestacao), 2, RoundingMode.HALF_UP);    	
    }
    
    
	 public  String getPrestacaoAtual() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",prestacao);
		 sPrestacao = sPrestacao.concat("/");
		 sPrestacao = sPrestacao.concat(String.format("%02d",nroPrestacao));
		 return sPrestacao; 
	 }
	 public BigDecimal getValorLanc() { 
		 if (conciliado)
			 return acertarSinal(valorPgto); 
		 return acertarSinal(valor);
	 }
		public BigDecimal getCredito() { 
			credito = BigDecimal.ZERO; 
			if (isCredito())
				credito =  this.valor.abs();
			
			return credito; 		
		}
		public BigDecimal getDebito() { 
			debito = BigDecimal.ZERO;
			if (isDebito()) 
				debito =  this.valor;
			
			return debito ; 		
		} 
	 /*
	  * Acerta o sinal conforme se Credito e Debito 
	  * 
	  * Debito =>  Negativo
	  * Credito => Positivo 
	  */
	 public BigDecimal acertarSinal( BigDecimal valor) { 
		 if (isDebito()) { 
			 return valor.abs().negate(); 
		 }
		 return valor.abs();
	 }
	public boolean isDebito() {
		return this.tipoLancamento == TipoLancamento.D;
	}
	public boolean isCredito() {
		return this.tipoLancamento == TipoLancamento.C;
	}
	public boolean isPositivo (BigDecimal valor) {
	  return (valor.compareTo(BigDecimal.ZERO) == 1); 	
	}
	public boolean isPositivo () {
		  return (this.valor.compareTo(BigDecimal.ZERO) == 1); 	
	}

	 
   
	 
	public BigDecimal calcularSaldo(BigDecimal saldoAnterior) {
		
		this.saldo = saldoAnterior.add(this.getCredito()).subtract(this.getDebito()); 
		return this.saldo; 		
	}
	public BigDecimal acertarSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return  saldo.add(this.valor); 				
	}


	
	public void setLancamentoSubCategoria(SubCategoria  subCategoria) { 
		
		this.subCategoria = subCategoria;
		if (subCategoria != null) 
			this.tipoLancamento = subCategoria.getTipoLancamento();
	}

	public boolean possuiContaLancanto() {
		return this.contaLancamento != null;  
	}
   
	public String getMesAnoLancamento() { 
		String mesAno; 
		if( dataVenc != null ) {
			mesAno = String.format("%02d",dataVenc.getMonthValue()); 
			mesAno = mesAno + dataVenc.getYear(); 
			return mesAno;					
		}
		mesAno = String.valueOf(dataDoc.getMonthValue());
		mesAno = mesAno + dataDoc.getYear(); 
		return mesAno;
	}
}
