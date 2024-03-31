package br.com.fenix.dominio.modelo.DadoBasico;

import java.beans.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;



@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS )
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)
public  abstract class MasterCategoria extends EntidadeAuditavel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    @Column(length = 40, nullable =  false)
	protected String descricao;
	

    @Column(length = 2, nullable =  false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected TipoLancamento tipoLancamento;
    
    @Column(name="inativo", nullable=true)
    @JsonInclude(content = Include.NON_NULL)
    protected boolean inativo; 
    
    @jakarta.persistence.Transient
    protected boolean debito;
    @jakarta.persistence.Transient
    protected boolean credito;
    
    public  MasterCategoria() {
    	super();
    	this.inativo = false;
    }
    
    public boolean GetDebito() {
    	boolean teste; 
    	teste = tipoLancamento == TipoLancamento.D;
    	System.out.println( "isDebito: " + teste);
    	return tipoLancamento == TipoLancamento.D; 
    }
    
    public boolean isDebito() {
    	return tipoLancamento == TipoLancamento.D; 
    }
    public boolean GetCredito() {
    	return tipoLancamento == TipoLancamento.C; 
    }
    
    public boolean isCredito() {
    	return tipoLancamento == TipoLancamento.C; 
    }
    public CategoriaDTO categoria_DTO() {
    	return new CategoriaDTO(this.getId(),descricao,tipoLancamento,0L,false,false);
    }
    
//    protected boolean despesaFixa;
    

//	@Transient
//	public String getTipoCategoria() {
//		 return this.getClass().getAnnotation(DiscriminatorValue.class).value();
//	}
}
