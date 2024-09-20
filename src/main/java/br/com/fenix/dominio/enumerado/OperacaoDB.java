package br.com.fenix.dominio.enumerado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OperacaoDB {
	INS("INS", "Inserção"), 
	UPT("UPT","Updade"),
	DEL("DEL","Delete");


	private String operacaoDB;
	private String descricao;
	
	OperacaoDB(String operacaoDB, String descricao) {
		this.operacaoDB = operacaoDB;
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipoOperacao() {
		return this.operacaoDB;
	}

	public void settipoOperacao(String operacaoDB) {
		this.operacaoDB = operacaoDB;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}		
}
