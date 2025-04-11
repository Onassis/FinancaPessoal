package br.com.fenix.dominio.enumerado;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

import br.com.fenix.dominio.modelo.Option;

public enum TipoCategoria {
	DP("DP", "Despesa"), 
	EP("EP", "Emprestimo"), 
	IV("IV", "Investimento"), 
	RE("RE", "Receita"), 
	TR("TR", "Transferência");


	private String tipo;
	private String descricao;
	
	TipoCategoria(String tipo, String descricao) {
		this.tipo = tipo;
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipoCategoria() {
		return this.tipo;
	}

	public void settipoCategoria(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	public static List<Option>  listaTipoCategoria() {
		   List<Option> options = Stream.of(TipoCategoria.values())
		            .map(tipo -> new Option(tipo.name(), tipo.getDescricao()))
		            .collect(Collectors.toList());
		return options;
	}
}
