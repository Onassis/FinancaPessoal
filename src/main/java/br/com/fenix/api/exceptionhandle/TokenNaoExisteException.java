package br.com.fenix.api.exceptionhandle;

public class TokenNaoExisteException extends NegocioException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TokenNaoExisteException( String messange) {
		super(messange);
	}

}
