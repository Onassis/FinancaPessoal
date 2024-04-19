package br.com.fenix.dominio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BooleanDeserializer;

import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.MoedaDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.converter.NumericBooleanDeserializer;
import br.com.fenix.dominio.converter.NumericBooleanSerializer;
import br.com.fenix.dominio.converter.SubCategoriaDeserializer;
import br.com.fenix.dominio.converter.UsuarioDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.seguranca.modelo.Usuario;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LancamentoDTO implements Comparable<LancamentoDTO> {
	
 
	
	private Long  idLancAux;
	private Long  detalheLancamentoId;
	private Long  detalheDestinoId;
	private Long  lancamentoId;
		
	@JsonDeserialize(using = UsuarioDeserializer.class)
    private Usuario criadoPor;
	 
	private String lancamentoInformacao;
	
    private String lancamentoObservacao;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate lancamentoDataDoc;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataVenc;
    
    protected TipoLancamento tipoLancamento;
    
	protected TipoOperacao lancamentoTipoOperacao;
	
	@JsonDeserialize(using = FavorecidoDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)	
    protected Favorecido lancamentoFavorecido;
	
    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected Conta contaLancamento ;
    
 // chave de lancamento da conta 
    protected String chaveBanco; 
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 

    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)
    protected Conta contaDestino ;

    @JsonDeserialize(using = SubCategoriaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected SubCategoria lancamentoSubCategoria;
      
    protected int lancamentoNroPrestacao;
    protected int lancamentoNroInicialPrestacao ;
    /**
     * Prestação atual do parcelamento
     */
    protected int prestacao;

    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal valor;
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal saldo;
    @Transient
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal credito;
    @JsonDeserialize(using = MoneyDeserializer.class) 
    @Transient
	protected BigDecimal debito;
    
   
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;
   
    public LancamentoDTO() {    	
    	super();
    	this.valor = BigDecimal.ZERO; 
    	this.credito = BigDecimal.ZERO; 
    	this.debito = BigDecimal.ZERO; 
        this.lancamentoNroPrestacao = 1;
        this.lancamentoNroInicialPrestacao = 1;
    	
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
	 
	public BigDecimal calculaSaldo(BigDecimal saldoAnterior) {
		
		this.saldo = saldoAnterior.add(this.getCredito()).subtract(this.getDebito()); 
		return this.saldo; 		
	}
	public BigDecimal acertaSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return  saldo.add(this.valor); 				
	}


	@Override
	public int compareTo(LancamentoDTO lanc) {
		
		if ( this.equals(lanc) ) {  
		   return 1;
		 } 
		
		return  0;
	}
	
	
	public void setLancamentoSubCategoria(SubCategoria  subCategoria) { 
		
		this.lancamentoSubCategoria = subCategoria;
		if (subCategoria != null) 
			this.tipoLancamento = subCategoria.getTipoLancamento();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LancamentoDTO other = (LancamentoDTO) obj;
		if (lancamentoId != null)  
		 	return Objects.equals(lancamentoId, other.lancamentoId);
		if (detalheDestinoId != null)  
		 	return Objects.equals(detalheDestinoId, other.detalheDestinoId);
		if (idLancAux != null)  
		 	return Objects.equals(idLancAux, other.idLancAux);
		return Objects.equals(this.toString(), other.toString()); 
			}
	
	@Override
	public int hashCode() {
		if (lancamentoId != null)  
		 	return Objects.hash(lancamentoId);
		if (detalheDestinoId != null)  
		 	return Objects.hash(detalheDestinoId);
		if (idLancAux != null)  
		 	return Objects.hash(idLancAux);
		
		return Objects.hash(this.toString());		
	}

	public boolean possuiContaLancanto() {
		return this.contaLancamento != null;  
	}

	
}
