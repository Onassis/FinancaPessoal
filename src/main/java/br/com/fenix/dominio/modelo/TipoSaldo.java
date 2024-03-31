package br.com.fenix.dominio.modelo;

public enum TipoSaldo {
	
		SI("SI", "Saldo Inicial"), 
		SD("SD", "Saldo");
	
	private String sigla;
	private String descricao;
	
	TipoSaldo(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
		
}
