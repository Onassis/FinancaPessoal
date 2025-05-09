package br.com.fenix.dominio.enumerado;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

import br.com.fenix.dominio.modelo.Option;

public enum TipoOperacao {
	AP("AP", "Aplicação"), 
	CC("CC","Cartão de crédito"),
	CH("CH","Cheque"),
	DB("DB","Débito"),
	DP("DP","Depósito"),
	EP("EP","Emprestimo"),
	ES("ES","Estorno"),
	PI("PI", "Pix"),
	PG("PG","Pagamento"),
	SQ("SQ","Saque"),
	RG("RG", "Resgate"),	
	TR("TR", "Transferência");


	private String tipoOperacao;
	private String descricao;
	
	TipoOperacao(String tipoOperacao, String descricao) {
		this.tipoOperacao = tipoOperacao;
		this.descricao = descricao;
	}
	@JsonValue
	public String getTipoOperacao() {
		return this.tipoOperacao;
	}

	public void settipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	public static List<Option>  listaTipoOperacao() {
		   List<Option> options = Stream.of(TipoOperacao.values())
		            .map(tipo -> new Option(tipo.name(), tipo.getDescricao()))
		            .collect(Collectors.toList());
		return options;
	}
}
