package br.com.fenix.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.ToString;

@ToString
public class Tag {
	
	private String tagNome=""; 
	private String tagValor="";

	public Tag() {
		super();
//	    tagNome=""; 
//	    tagValor="";		
	}
	
	public Tag(String textoLinha) {
		super();
//         tagNome=""; 
//    	   tagValor="";
        textoLinha = TextProcessor.removeEspaco(textoLinha); 
        
		int close = textoLinha.indexOf(">", 0);
		if (close > 0 ) {
			this.tagNome = textoLinha.substring(1, close);
			this.tagValor = textoLinha.substring(close+1, textoLinha.length()).toUpperCase();
		}
	}
	public void setTagNome(String tagNome) {
		this.tagNome = tagNome;
	}

	public String getTagNome() {
		return tagNome;
	}
	
	public void setTagValor(String tagValor) {
		this.tagValor = tagValor.toUpperCase();
	} 
	public String getTagValor() {
		return tagValor;
	}
	public BigDecimal getTagValorBigDecimal() {	
		
		return new  BigDecimal(tagValor);
	}
	public LocalDate getTagValorDate() {
	  String data = tagValor.substring(0,8);
	  return (LocalDate.parse(data, DateTimeFormatter.BASIC_ISO_DATE));
	}

}
