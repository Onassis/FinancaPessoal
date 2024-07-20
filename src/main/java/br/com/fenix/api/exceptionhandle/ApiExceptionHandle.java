package br.com.fenix.api.exceptionhandle;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import org.springframework.context.i18n.LocaleContextHolder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
//@ControllerAdvice
public class ApiExceptionHandle extends ResponseEntityExceptionHandler {
/**
	@Autowired
    private MessageSource messageSource; 
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
    	Problema problema = new Problema(HttpStatus.BAD_REQUEST, 
				ex.getMessage(),
				OffsetDateTime.now() );
    	System.out.println(problema);
        return new ResponseEntity<>(problema, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
	@ExceptionHandler(EntidadeNaoEncontratException.class)
	public ResponseEntity<Object> HandleNegocio(EntidadeNaoEncontratException ex, WebRequest webRequest) {
		Problema problema = new Problema(HttpStatus.NOT_FOUND, 
									ex.getMessage(),
									OffsetDateTime.now() );
		System.out.println(problema);
		return handleExceptionInternal(ex, problema,new HttpHeaders(),problema.getStatus(), webRequest);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> HandleNegocio(NegocioException ex, WebRequest webRequest) {
		Problema problema = new Problema(HttpStatus.BAD_REQUEST, 
									ex.getMessage(),
									OffsetDateTime.now() );
		System.out.println(problema);
		System.out.println(ex);
		
		return handleExceptionInternal(ex, problema,new HttpHeaders(),problema.getStatus(), webRequest);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> HandleNegocio(RuntimeException ex, WebRequest webRequest) {
		Problema problema = new Problema(HttpStatus.BAD_REQUEST, 
									ex.getMessage(),
									OffsetDateTime.now() );
		System.out.println(problema);
		System.out.println(ex);
		
		return handleExceptionInternal(ex, problema,new HttpHeaders(),problema.getStatus(), webRequest);
	}
	
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
								HttpHeaders headers, HttpStatus status, WebRequest webRequest) {		
	
		ArrayList<Problema.Campo> campos = new ArrayList<Problema.Campo>();
		
		Problema problema = new Problema(status,"Preenchimento incorreto",OffsetDateTime.now() );
		System.out.println(problema);	
		for (ObjectError err : ex.getBindingResult().getAllErrors()) {
					//					String mensagem = err.getDefaultMessage(
			        String nome = ((FieldError) err).getField(); 
					String mensagem = messageSource.getMessage(err, LocaleContextHolder.getLocale());
			campos.add(new Problema.Campo(nome, mensagem));		
			
		}
		problema.setCampos(campos);
		System.out.println("Trata error ");
		System.out.println(problema);
		return handleExceptionInternal(ex, problema, headers,status , webRequest);
	}
	**/
}

