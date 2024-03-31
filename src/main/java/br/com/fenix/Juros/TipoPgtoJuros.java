package br.com.fenix.Juros;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoPgtoJuros {
	AT("AT", "Antecipado"), 
	PO("PO", "Postecipado");

	private String tipoPgtoJuros;
	private String descricao;
	
	TipoPgtoJuros(String tipoPgtoJuros, String descricao) {
		this.tipoPgtoJuros = tipoPgtoJuros;
		this.descricao = descricao;
	}
	@JsonValue
	public String getPgtoJuros() {
		return tipoPgtoJuros;
	}

	public void setTipoPgtoJuros(String tipoPgtoJuros) {
		this.tipoPgtoJuros = tipoPgtoJuros;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
		
}
