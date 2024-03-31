package br.com.fenix.seguranca.email;

public class MessagingException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public MessagingException( String messange) {
		super(messange);
	}
}
