package br.com.fenix.dominio.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converter.ContaDeserializer;
import br.com.fenix.dominio.converter.MoneyDeserializer;
import br.com.fenix.dominio.converter.StringDeserializer;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.conta.Conta;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UploadDTO implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
 


		@JsonDeserialize(using = ContaDeserializer.class)    
		@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY , optional = true)	
		private Conta conta;
		
		private String mesCarga;
		
		@JsonDeserialize(using = MoneyDeserializer.class) 	
		private BigDecimal  saldoIni = BigDecimal.ZERO;
		
		
		@JsonDeserialize(using = StringDeserializer.class)		
		private String file;
		
		 @Lob
		 private byte[] data; 
		 

}
