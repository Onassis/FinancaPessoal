package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

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
import br.com.fenix.dominio.converter.rest.UsuarioDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.util.Util;
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

public abstract class AbstracLancamento<ID> extends AbstracDetLanc<ID> {
	  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//
	protected UUID  idLancAux;
	protected UUID  detalheLancamentoId;
///	
	protected Long  detalheDestinoId;
	protected UUID  lancamentoId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataDoc;
    protected TipoLancamento tipoLancamento;
	
	@JsonDeserialize(using = FavorecidoDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)	
    protected Favorecido favorecido;


    @JsonDeserialize(using = SubCategoriaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected SubCategoria subCategoria;
      
    protected int nroPrestacao=1;
    protected int nroInicialPrestacao=1 ;
    
    @JsonDeserialize(using = MoneyDeserializer.class) 
	protected BigDecimal total = BigDecimal.ZERO;
    
	@JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;
	
	
    /* Entrada em emprestimo */ 
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
   	protected boolean entrada ;
    
    protected String informacao;
	protected TipoOperacao tipoOperacao;
	@JsonDeserialize(using = UsuarioDeserializer.class)
	protected Usuario criadoPor;
	
    public AbstracLancamento(DetalheLancamento detLanc) {    	
    	

    	Lancamento lancamento = detLanc.getLancamento();

    	this.dataDoc = lancamento.dataDoc;
    	this.lancamentoId = lancamento.getId(); 
    	this.detalheLancamentoId = detLanc.getId();
        this.nroPrestacao = lancamento.getNroPrestacao();
        this.nroInicialPrestacao = lancamento.getNroInicialPrestacao();
        this.dataDoc = lancamento.getDataDoc(); 
        this.subCategoria  = lancamento.getSubCategoria(); 
        
        this.informacao = lancamento.getInformacao();
        
        this.conciliado = detLanc.isConciliado();
        
        
        this.criadoPor = detLanc.getCriadoPor(); 

        this.setLancamentoTotal(lancamento.getTotal().abs());
        
        this.valor = detLanc.getValor().abs();

        this.valorPgto = detLanc.getValorPgto().abs();
        
        this.valorLanc = detLanc.getValorLanc();

    	this.tipoOperacao = lancamento.getTipoOperacao();
 
    	this.favorecido = lancamento.getFavorecido(); 
    	
 	
    }
    public AbstracLancamento(Lancamento lancamento) {  
    	super(); 
     // 	this.id 		  = lancamento.getId(); 
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
    		DetalheLancamento detLanc = lancamento.getDetalheLancamento().get(0);
    		this.detalheLancamentoId = detLanc.getId();
    		this.tipoLancamento = detLanc.getTipoLancamento(); 
    		this.contaLancamento  = detLanc.getContaLancamento(); 
    		this.contaTransferencia = detLanc.getContaTransferencia(); 
    		this.valor	 = detLanc.getValor().abs();
            this.dataVenc = detLanc.getDataVenc(); 
            this.dataPgto = detLanc.getDataPgto();
            this.valorPgto = detLanc.getValorPgto().abs();
            this.valorLanc = detLanc.getValorLanc();
            this.dataRef  = detLanc.getDataRef();
            this.conciliado = detLanc.isConciliado();
    	} 
    }
   public AbstracLancamento() {    	
 	   super();
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
	public  String getPrestacaoAtual() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",prestacao);
		 sPrestacao = sPrestacao.concat("/");
		 sPrestacao = sPrestacao.concat(String.format("%02d",nroPrestacao));
		 return sPrestacao; 
	 }

	public void setLancamentoTotal( BigDecimal total) { 
    	this.total = acertarSinal(total); 
    }
    

	
	public void setLancamentoSubCategoria(SubCategoria  subCategoria) { 
		
		this.subCategoria = subCategoria;
		if (subCategoria != null) 
			this.tipoLancamento = subCategoria.getTipoLancamento();
	}
	

}
