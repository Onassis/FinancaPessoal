package br.com.fenix.api.exceptionhandle;

public class RegistroNaoExisteException extends NegocioException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RegistroNaoExisteException( String messange) {
		super(messange);
	}

}
