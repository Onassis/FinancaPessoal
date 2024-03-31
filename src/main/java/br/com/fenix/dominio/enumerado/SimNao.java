package br.com.fenix.dominio.enumerado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SimNao {
	S("S","Sim"), 
	N("N","Não");
	private String tipo;
	private String descricao;
	
	private SimNao(String tipo, String descricao) {
		this.tipo = tipo;
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}	
}
