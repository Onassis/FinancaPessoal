package br.com.fenix.dominio.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.seguranca.usuario.Usuario;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


public class TotalMesDetalheView implements Serializable {
	
	  /**
	   * 
	 * 
	 */

	private static final long serialVersionUID = 1L;
		@JsonDeserialize(using = ContaDeserializer.class)
		private Conta conta ;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate data;
	
	    @JsonIgnore
	    private Usuario criadoPor;	    
	    @JsonDeserialize(using = MoneyDeserializer.class) 
		private BigDecimal valor = BigDecimal.ZERO ;
	    
	    public TotalMesDetalheView (Conta conta) { 	    	
	    		this.conta = conta;
	    		valor = BigDecimal.ZERO ;	    
	    }  
}
