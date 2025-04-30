package br.com.fenix.fi.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.seguranca.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
public class LancDTO extends EntidadeAbstrata<Long> implements Comparable<LancDTO> {
	
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 2, nullable =  false)
    @Enumerated(EnumType.STRING)
	private TipoOperacao tipoOperacao;
   
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY ,optional = true )
    @JsonIgnore
    private Categoria categoria;

    @JsonDeserialize (using = SubCategoriaDeserializer.class)    
    @ManyToOne(cascade = CascadeType.PERSIST ,fetch = FetchType.EAGER ,optional = true  )
    private SubCategoria subCategoria;
    
    private Favorecido favorecido;
	
    @Column(length = 80)
	private String informacao;

    private String observacao;
    
    @Column(nullable = false, columnDefinition = "DATE")	
    private LocalDate dataDoc;

    private int nroPrestacao;
    private int nroInicialPrestacao;
    
    @Column(nullable = true)
    private boolean transferencia=false; 
    
	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
	private BigDecimal total;	

//	@JsonDeserialize(using = NumericBooleanDeserializer.class)
//	protected boolean conciliado ;
	
	@JsonDeserialize(using = UsuarioDeserializer.class)
    private Usuario criadoPor;

	private long versao;
	
    public LancDTO() {    	
    	super();    
    	this.total = BigDecimal.ZERO;
    	this.total = BigDecimal.ZERO; 
        this.nroPrestacao = 1;
        this.nroInicialPrestacao = 1;
    }

    
	 public String prestacao() {
		 String sPrestacao; 
		 sPrestacao = String.format("%02d",nroInicialPrestacao);
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


	public int compareTo(LancDTO lanc) {
		
		if ( this.equals(lanc) ) {  
		   return 1;
		 } 
		
		return  0;
	}
	
	
	
//	public boolean possuiContaLancanto() {
//		return this.contaLancamento != null;  
//	}

	
}
