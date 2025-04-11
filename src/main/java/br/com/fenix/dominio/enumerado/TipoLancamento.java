package br.com.fenix.dominio.enumerado;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

import br.com.fenix.dominio.modelo.Option;
import lombok.Getter;

@Getter
public enum TipoLancamento {
	C("C", "Credito"),
	D("D", "Debito");

	private String tipoLancamento;
	private String descricao;
	
	TipoLancamento(String tipoLancamento, String descricao) {
		this.tipoLancamento = tipoLancamento;
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipoLancamento() {
		return this.tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}		
	public static List<Option>  listaTipoLancamento() {
		   List<Option> options = Stream.of(TipoLancamento.values())
		            .map(tipo -> new Option(tipo.name(), tipo.getDescricao()))
		            .collect(Collectors.toList());
		return options;
	}
}
