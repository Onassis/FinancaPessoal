package br.com.fenix.api.exceptionhandle;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistroJaExisteException extends NegocioException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RegistroJaExisteException( String messange) {
		super(messange);
	}

}
