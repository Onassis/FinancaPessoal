package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Persistable;
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
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class LancamentoDTO implements Comparable<UUID>,Persistable<UUID> {
	
 
	
	/**
	 * 
	 */
	
	
	
	private static final long serialVersionUID = 1L;

	/*
	 *  Campos de detalhe lancamento 
	 */
	//
	protected UUID  idLancAux;
	protected UUID  detalheLancamentoId;
	protected UUID  lancamentoId;
///	
	protected Long  detalheDestinoId;
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataDoc;
    
	protected TipoLancamento tipoLancamento;
	
	protected TipoOperacao tipoOperacao;
	
	
	@JsonDeserialize(using = FavorecidoDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)	
    protected Favorecido favorecido;


    @JsonDeserialize(using = SubCategoriaDeserializer.class)
    @JsonInclude(content = Include.NON_NULL)    
    protected SubCategoria subCategoria;
      
    protected int nroPrestacao=1;
    protected int nroInicialPrestacao=1 ;
    

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
	
    protected String informacao;



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
	
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	protected BigDecimal total;

	@JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean conciliado ;

	@JsonDeserialize(using = NumericBooleanDeserializer.class)
	protected boolean entrada ;

	
	  
    public LancamentoDTO() {    	
    	super();
    	this.dataDoc = LocalDate.now();
    	this.dataVenc = LocalDate.now();
    	this.dataRef = LocalDate.now();
    	
    }

    
    public LancamentoDTO(Lancamento lancamento) {    	
    	super(); 
    	setLancamento(lancamento);
    	if  (lancamento.getDetalheLancamento().isEmpty() == false) {
    		setDetalheLancamento(lancamento.getDetalheLancamento().get(0));
    	}	
    }
    public LancamentoDTO(DetalheLancamento detLanc) {    	
        setLancamento(detLanc.getLancamento());
        setDetalheLancamento(detLanc);
    }

    private void setDetalheLancamento (DetalheLancamento detLanc) {    	
    		this.detalheLancamentoId = detLanc.getId();
    		this.tipoLancamento = detLanc.getTipoLancamento(); 
    		this.contaLancamento  = detLanc.getContaLancamento(); 
    		this.contaTransferencia = detLanc.getContaTransferencia(); 
    		this.valor	 = detLanc.getValor().abs();
            this.valorPgto = detLanc.getValorPgto().abs();
            this.valorLanc = detLanc.getValorLanc();
            this.dataVenc = detLanc.getDataVenc(); 
            this.dataPgto = detLanc.getDataPgto();    
            this.dataRef  = detLanc.getDataRef();
            this.conciliado = detLanc.isConciliado();
            
    	} 
    private void setLancamento(Lancamento lancamento) {
    	this.lancamentoId = lancamento.getId(); 
        this.nroPrestacao = lancamento.getNroPrestacao();
        this.nroInicialPrestacao = lancamento.getNroInicialPrestacao();
        this.dataDoc = lancamento.getDataDoc(); 
        this.total  = lancamento.getTotal();    	     
        this.entrada = lancamento.isEntrada();
        this.subCategoria  = lancamento.getSubCategoria();     	        
        this.informacao = lancamento.getInformacao();
    	this.tipoOperacao = lancamento.getTipoOperacao();
    	this.favorecido = lancamento.getFavorecido(); 
    	this.entrada = lancamento.isEntrada();

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

	@Override
	public UUID getId() {
		UUID id = (idLancAux != null ) ? idLancAux : detalheLancamentoId;
		id = (id != null) ? id :  lancamentoId;
		return id;
	}

	@Override
	public boolean isNew() {	
		return getId() == null ;
	}

	@Override
	public int compareTo(UUID o) {
		return getId().compareTo(o); 	
	}
	
}
