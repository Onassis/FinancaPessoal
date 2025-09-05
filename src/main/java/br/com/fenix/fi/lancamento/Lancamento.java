package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.fenix.abstrato.base.EntidadeAuditavel;
import br.com.fenix.abstrato.base.EntidadeAuditavelAuto;
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
public  class Lancamento extends EntidadeAuditavelAuto<UUID> {

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
    	LocalDate data = lancAux.getDataLanc();
    	
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
					.dataRef(data)
					.dataPgto(data) 
					.chaveBanco(lancAux.getChaveBanco()) 
					.refBanco(lancAux.getRefBanco()) 
					.contaLancamento(lancAux.getContaLancamento())
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
	public Optional<DetalheLancamento> filtroPorDetalheId (UUID id) { 
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

}
