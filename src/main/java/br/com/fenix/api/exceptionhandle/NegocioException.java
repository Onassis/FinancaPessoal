package br.com.fenix.api.exceptionhandle;

public class NegocioException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NegocioException( String messange) {
		super(messange);
	}

}
