package br.com.fenix.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converterRest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Favorecido;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(callSuper=true)
@Entity 
@Table(name="lancamento", indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
public class Lancamento extends EntidadeAuditavel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   
    @Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoOperacao tipoOperacao;
   
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY ,optional = true  )
	@JsonBackReference
    private Favorecido favorecido;
	
    @Column(length = 80)
	private String informacao;

    private String observacao;
    
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY ,optional = true )
    @JsonIgnore
    private Categoria categoria;

    @JsonDeserialize (using = SubCategoriaDeserializer.class)    
    @ManyToOne(cascade = CascadeType.PERSIST ,fetch = FetchType.EAGER ,optional = true  )
    private SubCategoria subCategoria;
    
 
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "lancamento")
    private List<DetalheLancamento> datalheLancamento = new ArrayList<DetalheLancamento>()  ;    	

    @Column(nullable = false, columnDefinition = "DATE")	
    private LocalDate dataDoc;

    private int nroPrestacao;
    private int nroInicialPrestacao;
    
    @Column(nullable = true)
    private boolean transferencia=false; 
    
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal total;

	
	public Lancamento() {
		super();
	    this.nroPrestacao = 1;
	    this.nroInicialPrestacao = 1;				
	}
	
	public Lancamento (LocalDate dataDoc,TipoOperacao tipoOperacao,  BigDecimal valor,BigDecimal saldo ) {
		this.dataDoc = dataDoc; 
	    this.nroPrestacao = 1;
	    this.nroInicialPrestacao = 1;		
		this.tipoOperacao = tipoOperacao;
		this.total = valor; 
	}
	public void addDatalheLancamento(DetalheLancamento detalheLancamento) {
        detalheLancamento.setLancamento(this); 
        this.datalheLancamento.add(detalheLancamento);  		
	}
	

/*	 public void setSubCategoria(SubCategoria subCategoria) {
	    	this.subCategoria = subCategoria; 
	    	this.categoria = subCategoria.getCategoria();
	    }
/*	
	public void sacar (LocalDate dataDoc, Conta conta, Favorecido favorecido,MasterCategoria categoria, BigDecimal valor ) {
		this.dataDoc = dataDoc; 
		this.diaVecto   = dataDoc.getDayOfMonth();
	    this.nroPrestacao = 1;
	    this.nroInicialPrestacao = 1;		
//		this.tipoOperacao = tipoOperacao.D;
//		this.tipoLancamento = TipoLancamento.D;
//		this.categoria = categoria;
		this.valor = valor; 
//        DetalheLancamento detalheLancamento = new DetalheLancamento(//this, dataDoc,conta, favorecido, valor);
//		this.addDatalheLancamento(detalheLancamento);		
	}

	
	public void depositar (LocalDate dataDoc,TipoOperacao tipoOperacao,  BigDecimal valor ) {
		this.dataDoc = dataDoc; 
		this.diaVecto   = dataDoc.getDayOfMonth();
	    this.nroPrestacao = 1;
	    this.nroInicialPrestacao = 1;
//		this.tipoOperacao = tipoOperacao;
//		this.tipoLancamento = TipoLancamento.C;
		this.valor = valor; 
//		this.saldo.add(valor);	
//		DetalheLancamento detalheLancamento = new DetalheLancamento(dataCompra,this.conta,this.tipoLancamento,valor);
//		this.addDatalheLancamento(detalheLancamento);
	}

	public void comprarCartao (LocalDate dataDoc,  BigDecimal valor ) {
		this.dataDoc = dataDoc; 
//		this.diaVecto   = this.conta.getDiaVencimento();
		this.nroPrestacao = 1;
		this.nroInicialPrestacao = 1;
//		this.tipoOperacao = TipoOperacao.D;
//		this.tipoLancamento = TipoLancamento.D;
		this.valor = valor; 
//		this.saldo.add(valor);		
	}	
	public void comprarCartaoParcelado (LocalDate dataDoc,  int nroPrestacao,
										int nroInicialPrestacao, BigDecimal valor ) {
		this.dataDoc = dataDoc; 
//		this.diaVecto   = this.conta.getDiaVencimento();
		this.nroPrestacao = nroInicialPrestacao;
		this.nroInicialPrestacao = nroPrestacao;
//		this.tipoOperacao = TipoOperacao.D;
//		this.tipoLancamento = TipoLancamento.D;
		this.valor = valor.divide( new BigDecimal(nroPrestacao)); 
//		this.saldo.add(this.valor);		
	}
//	public void addDatalheLancamento(DetalheLancamento datalheLancamento) {
//		this.datalheLancamento.add(datalheLancamento); 
//	}	
*/	

	

}
