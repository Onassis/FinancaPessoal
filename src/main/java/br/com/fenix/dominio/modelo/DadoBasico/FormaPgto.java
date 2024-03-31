package br.com.fenix.dominio.modelo.DadoBasico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import br.com.fenix.abstrato.EntidadeAbstrata;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FormaPgto extends EntidadeAbstrata<Long> {
	
	private static final long serialVersionUID = -4594764148388431106L;
	
	@NotBlank
	@Column(length = 60,nullable = false)
	private String nome;
	
	private String icon;
	
}
