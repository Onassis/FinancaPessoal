package br.com.fenix.api.exceptionhandle;

public class RegistroJaExisteException extends NegocioException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RegistroJaExisteException( String messange) {
		super(messange);
	}

}
