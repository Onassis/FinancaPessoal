package br.com.fenix.dominio.modelo.DadoBasico;

import java.io.Serializable;

import jakarta.persistence.*;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "moeda")
public class Moeda implements Serializable  {
	/**
	 * ISO 4217 é um padrão internacional que define códigos de três letras para as 
	 * moedas correntes estabelecido pela Organização Internacional 
	 * para Padronização (pt-BR) ou Organização Internacional de Normalização (pt-PT)
	 */
	private static final long serialVersionUID = 1L;
	@Id @Nullable
	@Column(length = 3, nullable =  false, unique = true)	
	private String codigo;
	
	@Column(length = 3, nullable =  false, unique = true)
	private String numero;
	
	@Column(length = 4, nullable =  false)
	private String casaDecimal;
	
	@Column(length = 40, nullable =  false)	
	private String moeda;


	public Moeda() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Moeda other = (Moeda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Moeda [moeda=" + moeda + "]";
	}
	

}
