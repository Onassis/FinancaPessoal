package br.com.fenix.dominio.modelo.DadoBasico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

import br.com.fenix.abstrato.EntidadeAbstrata;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Operacao extends EntidadeAbstrata<Long> {
	
	private static final long serialVersionUID = -4594764148388431106L;
	
	@NotBlank
	@Column(length = 2,nullable = false)
	private String tipoOperacao;
	
	
	@ElementCollection(targetClass = TipoConta.class)
	@CollectionTable(name="OpTipoConta",joinColumns=@JoinColumn(name="id"))
	@Enumerated(EnumType.STRING) 
	private Set<TipoConta> tipoConta;
	   
	@ElementCollection(targetClass = TipoLancamento.class)
	@CollectionTable(name="OpTipoLancamento", joinColumns=@JoinColumn(name="id"))
	@Enumerated(EnumType.STRING) 	
	private Set<TipoLancamento> tipoLanc;
	
	private String descricao;
	
	private String icon;
	
	
	
}
