package br.com.fenix.dominio.modelo.DadoBasico;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import br.com.fenix.abstrato.EntidadeAuditavel;
import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.MoedaDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.converter.StringDeserializer;
import br.com.fenix.dominio.converter.SubCategoriaDeserializer;
import br.com.fenix.dominio.enumerado.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="conta", indexes = { @Index(name = "contaApelido", columnList = "criado_por_id,apelido", unique = true) })  
@Data
@ToString
@NoArgsConstructor
public class Conta extends EntidadeAuditavel<Long> {
   /**
	 * 
	 */
   private static final long serialVersionUID = 1L;


   @Enumerated(EnumType.STRING) 
   private TipoConta tipoConta;
   
   @NotBlank
   @Column(length = 40, nullable=false )
   private String apelido;
   
   @Column(length = 40, nullable = true)
   private String instituicao; 
   
   @Column(length = 4 )
   private String agencia;
   
   @Column(length = 12 )
   private String numero;
   
   @Column(nullable=true)
   private int diaVencimento;
   
   // melhor dia de compra
   @Column(nullable=true)   
   private int diaComp;

   @JsonDeserialize(using = MoedaDeserializer.class) 
   @ManyToOne (fetch = FetchType.EAGER )     
   private Moeda moeda;
   
   @Column(name="inativo", nullable=false)
   private boolean inativo = false; 

   @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
   @JsonDeserialize(using = MoneyDeserializer.class) 
   @Column(columnDefinition = "DECIMAL(13,2) DEFAULT 0.00")
   private BigDecimal saldo;
   
   @Transient
   private String ajuda ;

  
   public String getAjuda() {
	   switch (this.tipoConta) {
		   case CC :   return  apelido + ":" + instituicao + " -> " +  numero ;
		   case CR :   return  apelido + ":" + instituicao + " -> " + agencia + "/" + numero ;
		   default:    return  apelido  ;
	   	}   
   }
   public boolean isCredito() { 
	   return tipoConta == TipoConta.CR; 
   }
   
   public LocalDate getDataSaldo() {
	   return getDataSaldo(LocalDate.now());
   }
   
   public LocalDate getDataFatura( LocalDate data) {
	   
	    LocalDate datVenc=data;
	    int diaCompra; 
	    
		if ( !isCredito() ) {
			return data;
		}
		diaCompra = data.getDayOfMonth();
		
		datVenc = LocalDate.of(datVenc.getYear(), datVenc.getMonthValue(), diaVencimento);
		if  (!datVenc.isAfter(data) )	{
			datVenc = datVenc.plusMonths(1);
		}
		if ( diaCompra  >=  this.diaComp) 
		{
			datVenc = datVenc.plusMonths(1);
		}
		return datVenc;
   }
   public LocalDate getDataSaldo( LocalDate dataSaldo) {
	   
	
	    	
		if ( isCredito() ) {		
			if ( dataSaldo.getDayOfMonth() -  this.diaVencimento > 9) {
				dataSaldo.plusMonths(1);
				dataSaldo = LocalDate.of(dataSaldo.getYear(), dataSaldo.getMonth(), diaVencimento);
			}
			else 
 		
			dataSaldo = LocalDate.of(dataSaldo.getYear(), dataSaldo.getMonth(), diaVencimento); 
		}
		else {
		    if ( dataSaldo.isAfter(LocalDate.now()) ) { 
		    	dataSaldo = LocalDate.now();
		    }
		
			dataSaldo = LocalDate.of(dataSaldo.getYear(), dataSaldo.getMonth(), 1);
		}	

		return dataSaldo; 		
	
}
   public boolean contaIsCartao() {
	   return this.tipoConta == TipoConta.CR; // Cartaa de credito 
   }
   public LocalDate dataSaldoAnterior (LocalDate data) {
	   
		if ( contaIsCartao()) { 		
			if ( ( data.getDayOfMonth() -  diaVencimento) <=  9) {
				data.plusMonths(1);
				return  LocalDate.of(data.getYear(), data.getMonth(), diaVencimento );
			}
			else 				
		 	  return  LocalDate.of(data.getYear(), data.getMonth(), diaVencimento);
		
		}
		return 	LocalDate.of(data.getYear(), data.getMonth(), 1 );
  }
}