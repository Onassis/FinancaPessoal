package br.com.fenix.dominio.enumerado;

import com.fasterxml.jackson.annotation.JsonValue;

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
}
