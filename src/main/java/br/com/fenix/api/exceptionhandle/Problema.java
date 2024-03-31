package br.com.fenix.api.exceptionhandle;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
public class Problema {
	
	private HttpStatus status;
	private OffsetDateTime datahora;
	private String titulo;
	private List<Campo> campos;
	@Getter
	@Setter
	@ToString
	public static class Campo {
		private String campo;
		private String mensage;
		public String getCampo() {
			return campo;
		}
		public Campo(String campo, String mensage) {
			super();
			this.campo = campo;
			this.setMensage(mensage);
		}
		
	}
	public Problema(HttpStatus status,  String titulo, OffsetDateTime datahora) {
		super();
		this.status = status;
		this.datahora = datahora;
		this.titulo = titulo;
	}

}
