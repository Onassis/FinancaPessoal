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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class LancamentoDTO extends AbstracLancamento<Long> implements Comparable<LancamentoDTO> {
	
 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long  idLancAux;
	private Long  detalheLancamentoId;
	
//	private Long  detalheDestinoId;
	private Long  lancamentoId;
	
	 
	private String informacao;

    
	protected TipoOperacao tipoOperacao;
	    
	@JsonDeserialize(using = UsuarioDeserializer.class)
    private Usuario criadoPor;
    
	  
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
    		DetalheLancamento detLanc = lancamento.getDetalheLancamento().get(0);
    		this.detalheLancamentoId = detLanc.getId();
    		this.tipoLancamento = detLanc.getTipoLancamento(); 
    		this.contaLancamento  = detLanc.getContaLancamento(); 
    		this.contaTransferencia = detLanc.getContaTransferencia(); 
    		this.valor	 = detLanc.getValor();
            this.dataVenc = detLanc.getDataVenc(); 
            this.dataPgto = detLanc.getDataPgto();
            this.valorPgto = detLanc.getValorPgto();
            this.dataRef  = detLanc.getDataRef();
            this.conciliado = detLanc.isConciliado();
            ajustaDataRef();
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
    	this.valor	 = detLanc.getValor().abs();
        this.dataVenc = detLanc.getDataVenc(); 
        this.dataPgto = detLanc.getDataPgto();
        this.valorPgto = detLanc.getValorPgto().abs();
        this.dataRef  = detLanc.getDataRef();

        this.conciliado = detLanc.isConciliado();
        ajustaDataRef();
    }

	@Override
	public int compareTo(LancamentoDTO o) {

		if ( this.equals(o) ) {  
		   return 1;
		 } 
		
		return  0;
	} 
   
	
}
