package br.com.fenix.dominio.enumerado;

import com.fasterxml.jackson.annotation.JsonValue;

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
	
	
}
