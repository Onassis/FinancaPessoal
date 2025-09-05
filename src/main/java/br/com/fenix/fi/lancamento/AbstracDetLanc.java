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
import br.com.fenix.abstrato.base.EntidadeAbstrataAuto;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.converter.rest.NumericBooleanDeserializer;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.util.Util;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public abstract class AbstracDetLanc<ID> extends EntidadeAbstrataAuto<ID>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 *  Campos de detalhe lancamento 
	 */

	protected TipoLancamento tipoLancamento;

	// chave de lancamento da conta 
	protected String chaveBanco;

	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	protected String refBanco; 

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate dataVenc;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate dataPgto;


	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate dataRef;

	@Column(nullable = false)
	private int ano;
	@Column(nullable = false)
	private int mes;

	@JsonDeserialize(using = ContaDeserializer.class)
	@JsonInclude(content = Include.NON_NULL)    
	protected Conta contaLancamento ;


	@JsonDeserialize(using = ContaDeserializer.class)
	@JsonInclude(content = Include.NON_NULL)
	protected Conta contaTransferencia ;

	/**
	 * Prestação atual do parcelamento
	 */
	protected int prestacao;


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


	public AbstracDetLanc() {    	    	
		this.dataVenc = LocalDate.now();   	
	}

	 public AbstracDetLanc(DetalheLancamento detLanc) {    
		   	tipoLancamento = detLanc.getTipoLancamento(); 
	    	contaLancamento  = detLanc.getContaLancamento(); 
	    	contaTransferencia = detLanc.getContaTransferencia(); 
	    	valor	 = detLanc.getValor().abs();
	        dataVenc = detLanc.getDataVenc(); 
	        dataPgto = detLanc.getDataPgto();
	        valorPgto = detLanc.getValorPgto().abs();
	        dataRef  = detLanc.getDataRef();
	        conciliado = detLanc.isConciliado();
	        ajustaDataRef();
	 }


	/*
	 * Ao atualizar Data Doc atualiza data Vencimento
	 */

	public void setDataPgto(LocalDate dataPgto) {
		this.dataPgto = dataPgto; 
		ajustaDataRef();
	}
	protected void ajustaDataRef() {
		dataRef = (conciliado) ? dataPgto :  dataVenc; 
	}
	/*
	 * Retorna o valor do lancamento 
	 */
	public BigDecimal getValorLanc() {
		valorLanc = (conciliado) ? acertarSinal(valorPgto) : acertarSinal(valor); 
		return valorLanc;
	}
	public BigDecimal getCredito() {
		return credito = (isCredito()) ? getValorLanc() : BigDecimal.ZERO; 		
	}
	public BigDecimal getDebito() {
		return debito = (isDebito()) ? getValorLanc().negate() : BigDecimal.ZERO; 		
	}
	public void setConciliado ( boolean conciliado) {
		this.conciliado = conciliado; 
		ajustarDataRef(); 
	}
	public void ajustarDataRef() {

		dataRef = (conciliado) ? dataPgto : dataVenc; 
		this.ano =  dataRef.getYear();
		this.mes =  dataRef.getMonthValue() ;	
	}

	/*
	 * Acerta o sinal conforme se Credito e Debito 
	 * 
	 * Debito =>  Negativo
	 * Credito => Positivo 
	 */
	public BigDecimal acertarSinal( BigDecimal valor) { 
		return  (isDebito()) ? valor.abs().negate() : valor.abs(); 	
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

	public boolean possuiContaLancanto() {
		return this.contaLancamento != null;  
	}

}
