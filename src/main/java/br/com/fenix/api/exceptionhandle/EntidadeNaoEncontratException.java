package br.com.fenix.api.exceptionhandle;

public class EntidadeNaoEncontratException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontratException( String messange) {
		super(messange);
	}

}
