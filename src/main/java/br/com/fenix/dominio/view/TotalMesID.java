package br.com.fenix.dominio.view;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.fenix.dominio.converterRest.ContaDeserializer;
import br.com.fenix.fi.conta.Conta;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TotalMesID implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonDeserialize(using = ContaDeserializer.class)
    private Conta conta ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
}
