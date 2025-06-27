package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.dominio.converter.rest.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.fi.upload.LancAux;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity 
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoOperacao", discriminatorType = DiscriminatorType.STRING)
@Table(name="lancamento", indexes = {@Index(name = "idx_usuario", columnList = "criado_por_id")})
public  class Lancamento extends EntidadeAuditavel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   
    @Column(length = 2, nullable =  false,insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
	protected TipoOperacao tipoOperacao;
    
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER ,optional = true )
    @JsonIgnore
    protected Categoria categoria;

    @JsonDeserialize (using = SubCategoriaDeserializer.class)    
    @ManyToOne(cascade = CascadeType.DETACH ,fetch = FetchType.EAGER ,optional = true  )
    protected SubCategoria subCategoria;
    
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER ,optional = true  )
	@JsonBackReference
	protected Favorecido favorecido;
	
    @Column(length = 80)
    protected String informacao;

//    protected String observacao;
    
 //   @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "lancamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )    
    @Singular("detalheLancamento")
    protected final List<DetalheLancamento> detalheLancamento = new ArrayList<DetalheLancamento>();  ;    	

    @Column(nullable = false, columnDefinition = "DATE")	
    protected LocalDate dataDoc;

    protected int nroPrestacao;
    protected int nroInicialPrestacao;
    
//    @Column(nullable = true)
//    @Transient
//    private boolean transferencia=false; 
    
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	protected BigDecimal total;



	
	public Lancamento() {
		super();
//	    this.nroPrestacao = 1;
//	    this.nroInicialPrestacao = 1;		
	  //  detalheLancamento = new ArrayList<DetalheLancamento>() ;
	}
    public Lancamento(LancAux lancAux) {
    	super();
    	LocalDate data = lancAux.getDataVenc();
    	
        this.nroPrestacao = lancAux.getNroPrestacao();
        this.nroInicialPrestacao = lancAux.getNroInicialPrestacao();
        this.dataDoc = lancAux.getDataDoc();
        setSubCategoria( lancAux.getSubCategoria()); 
      

        this.informacao = lancAux.getInformacao(); 
        this.setTotal(lancAux.getTotal());
    	this.tipoOperacao = lancAux.getTipoOperacao();
    	this.favorecido = lancAux.getFavorecido();
    
		for(int count=lancAux.getNroInicialPrestacao() ; count <= lancAux.getNroPrestacao(); count++){
			DetalheLancamento detalheLancamento = new DetalheLancamento().builder() 
					.prestacao(count)
					.valor(lancAux.getValor()) 
					.tipoLancamento(lancAux.getTipoLancamento())
					.dataVenc(data)
					.contaLancamento(lancAux.getContaLanc())
					.ano(data.getYear()) 
					.mes(data.getMonthValue())
					.conciliado(true) 
					.build();
			this.addDatalheLancamento(detalheLancamento);
			data = data.plusMonths(1);
		}    	
    }
	protected Lancamento(LancamentoDTO dto  ) {
		super(); 
		this.dataDoc = dto.dataDoc;  	    		
	    this.nroInicialPrestacao = dto.nroInicialPrestacao; 
		this.nroPrestacao = dto.nroPrestacao;
	    this.informacao = dto.getInformacao(); 
//	    this.observacao = dto.getObservacao();
    	this.subCategoria = dto.subCategoria;  
	    this.favorecido = dto.favorecido;
//        this.tipoOperacao(dto.tipoOperacao)
 	   this.total = dto.total;
	    
	}
	
	public Lancamento (LocalDate dataDoc,TipoOperacao tipoOperacao,  BigDecimal valor,BigDecimal saldo ) {
		this.dataDoc = dataDoc; 
	    this.nroPrestacao = 1;
	    this.nroInicialPrestacao = 1;		
		this.tipoOperacao = tipoOperacao;
		this.total = valor; 
	    //detalheLancamento = new ArrayList<DetalheLancamento>() ;
	}
	public Boolean isTransferencia() {
		return tipoOperacao == TipoOperacao.TR;
		
	}
	public Optional<DetalheLancamento> filtroPorDetalheId (Long id) { 
	  return    detalheLancamento.stream()	
						.filter(d -> d.getId() == id)
						.findFirst();
	}
	public Lancamento (TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	public void setSubCategoria(SubCategoria subCateroria) {
		if (subCateroria != null) {
			this.subCategoria = subCateroria;	
			this.categoria = subCateroria.getCategoria();
		}
	}
	public void addDatalheLancamento(DetalheLancamento detalheLac) {
		detalheLac.setLancamento(this); 
		detalheLancamento.add(detalheLac);  		
	}
    public BigDecimal getValorPrestacao() {   
        return total.divide(new BigDecimal(nroPrestacao), 2, RoundingMode.HALF_UP);    	
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
