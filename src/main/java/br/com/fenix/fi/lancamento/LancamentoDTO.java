package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BooleanDeserializer;

import br.com.fenix.abstrato.base.EntidadeAbstrata;
import br.com.fenix.dominio.converter.rest.ContaDeserializer;
import br.com.fenix.dominio.converter.rest.FavorecidoDeserializer;
import br.com.fenix.dominio.converter.rest.MoedaDeserializer;
import br.com.fenix.dominio.converter.rest.MoneyDeserializer;
import br.com.fenix.dominio.converter.rest.NumericBooleanDeserializer;
import br.com.fenix.dominio.converter.rest.NumericBooleanSerializer;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.dominio.converter.rest.UsuarioDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.fi.upload.LancAux;
import br.com.fenix.seguranca.usuario.Usuario;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LancamentoDTO extends EntidadeAbstrata<Long> implements Comparable<LancamentoDTO> {
	
 
	
	private Long  idLancAux;
	private Long  detalheLancamentoId;
	
//	private Long  detalheDestinoId;
	private Long  lancamentoId;
	
	 
	private String informacao;
	
//    private String Observacao;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataDoc;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataVenc;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataPgto;
    
    
    protected TipoLancamento tipoLancamento;
    
	protected TipoOperacao tipoOperacao;
	
	@JsonDeserialize(using = FavorecidoDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)	
    protected Favorecido favorecido;
	
    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected Conta contaLancamento ;
    
 // chave de lancamento da conta 
    protected String chaveBanco;
    
	// Nro de referencia REFNUM do arquivo OFX (Ex. Nro do cheque)  
	private String refBanco; 

    @JsonDeserialize(using = ContaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)
    protected Conta contaTransferencia ;

    @JsonDeserialize(using = SubCategoriaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected SubCategoria subCategoria;
      
    protected int nroPrestacao;
    protected int nroInicialPrestacao ;
    
    /**
     * Prestação atual do parcelamento
     */
     protected int prestacao;

    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal total;
    
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal valor;
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal valorPgto;
    
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
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
   	protected boolean entrada ;
    
	@JsonDeserialize(using = UsuarioDeserializer.class)
    private Usuario criadoPor;
	
	@Transient
	private String prestacaoAtual;
	  
    public LancamentoDTO() {    	
    	super();
    	this.dataDoc = LocalDate.now();
    	this.dataVenc = LocalDate.now();
//    	this.valor = BigDecimal.ZERO; 
//    	this.total = BigDecimal.ZERO;
//    	this.credito = BigDecimal.ZERO; 
//    	this.debito = BigDecimal.ZERO; 
//        this.nroPrestacao = 1;
//        this.nroInicialPrestacao = 1;
    	
    }

    public LancamentoDTO(Lancamento lancamento) {    	
    	super();
    	this.id 		  = lancamento.getId(); 
    	this.lancamentoId = lancamento.getId(); 
        this.nroPrestacao = lancamento.getNroPrestacao();
        this.nroInicialPrestacao = lancamento.getNroInicialPrestacao();
        this.dataDoc = lancamento.getDataDoc(); 
        this.subCategoria  = lancamento.getSubCategoria(); 
        
         this.informacao = lancamento.getInformacao();
        
        this.criadoPor = lancamento.getCriadoPor(); 

        this.setLancamentoTotal(lancamento.getTotal());

    	this.tipoOperacao = lancamento.getTipoOperacao();
 
    	this.favorecido = lancamento.getFavorecido(); 
    	  
    	if  (lancamento.getDetalheLancamento().isEmpty() == false) {
    		DetalheLancamento delLanc = lancamento.getDetalheLancamento().get(0);
    		this.detalheLancamentoId = delLanc.getId();
    		this.tipoLancamento = delLanc.getTipoLancamento(); 
    		this.contaLancamento  = delLanc.getContaLancamento(); 
    		this.contaTransferencia = delLanc.getContaTransferencia(); 
    		this.valor	 = delLanc.getValor();
            this.dataVenc = delLanc.getDataVenc(); 
            this.conciliado = delLanc.isConciliado();
    		
    	}      
   }
    public LancamentoDTO(DetalheLancamento detLanc) {    	
    	super();
    	
    	Lancamento lancamento = detLanc.getLancamento();
    	
    	this.id = detLanc.getId();
    	
    	this.lancamentoId = lancamento.getId(); 
        this.nroPrestacao = lancamento.getNroPrestacao();
        this.nroInicialPrestacao = lancamento.getNroInicialPrestacao();
        this.dataDoc = lancamento.getDataDoc(); 
        this.subCategoria  = lancamento.getSubCategoria(); 
        
        this.informacao = lancamento.getInformacao();
        
        this.criadoPor = lancamento.getCriadoPor(); 

        this.setLancamentoTotal(lancamento.getTotal());

    	this.tipoOperacao = lancamento.getTipoOperacao();
 
    	this.favorecido = lancamento.getFavorecido(); 
    	

    	this.prestacao = detLanc.getPrestacao();

   		this.detalheLancamentoId = detLanc.getId();
//    		this.detalheDestinoId = lancamento.getDetalheLancamento().get(0).getId();
    	this.tipoLancamento = detLanc.getTipoLancamento(); 
    	this.contaLancamento  = detLanc.getContaLancamento(); 
    	this.contaTransferencia = detLanc.getContaTransferencia(); 
    	this.valor	 = detLanc.getValor();
        this.dataVenc = detLanc.getDataVenc(); 
        this.conciliado = detLanc.isConciliado();    		
    } 
   
    public void setLancamentoTotal( BigDecimal total) { 
    	this.total = acertaSinal(total); 
    	this.valor = total.divide(new BigDecimal(nroPrestacao), 2, RoundingMode.HALF_UP);    	
    }
    
    
	 public  String getPrestacaoAtual() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",prestacao);
		 sPrestacao = sPrestacao.concat("/");
		 sPrestacao = sPrestacao.concat(String.format("%02d",nroPrestacao));
		 return sPrestacao; 
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
		
		this.subCategoria = subCategoria;
		if (subCategoria != null) 
			this.tipoLancamento = subCategoria.getTipoLancamento();
	}
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		LancamentoDTO other = (LancamentoDTO) obj;
//		if (lancamentoId != null)  
//		 	return Objects.equals(lancamentoId, other.lancamentoId);
//		if (detalheDestinoId != null)  
//		 	return Objects.equals(detalheDestinoId, other.detalheDestinoId);
//		if (idLancAux != null)  
//		 	return Objects.equals(idLancAux, other.idLancAux);
//		return Objects.equals(this.toString(), other.toString()); 
//			}
//	
//	@Override
//	public int hashCode() {
//		if (lancamentoId != null)  
//		 	return Objects.hash(lancamentoId);
//		if (detalheDestinoId != null)  
//		 	return Objects.hash(detalheDestinoId);
//		if (idLancAux != null)  
//		 	return Objects.hash(idLancAux);
//		
//		return Objects.hash(this.toString());		
//	}

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
