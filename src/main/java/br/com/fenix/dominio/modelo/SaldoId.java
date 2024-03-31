package br.com.fenix.dominio.modelo;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SaldoId implements Serializable {
	
	
	private TipoSaldo tipo; 
	@ManyToOne(optional = false)
 	private Conta conta ;
    @Column(columnDefinition = "DATE")	
    private LocalDate data;
    

}
