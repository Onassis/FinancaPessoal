package br.com.fenix.dominio.enumerado;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

import br.com.fenix.dominio.modelo.Option;

public enum TipoConta {
	CC("CC", "Conta corrente"), 
	CR("CR", "Cartão de credito"),
	DC("DC", "Dinheiro/Carteira"),
	IV("IV", "Investimento"),
	PO("PO", "Poupança");

	private String tipoConta;
	private String descricao;
	
	TipoConta(String tipoConta, String descricao) {
		this.tipoConta = tipoConta;
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public static List<Option>  listaTipoConta() {
		   List<Option> options = Stream.of(TipoConta.values())
		            .map(tipo -> new Option(tipo.name(), tipo.getDescricao()))
		            .collect(Collectors.toList());
		return options;
	}
	
	
}
