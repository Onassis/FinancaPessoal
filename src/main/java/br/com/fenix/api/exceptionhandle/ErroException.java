package br.com.fenix.api.exceptionhandle;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@RestControllerAdvice
public class ErroException {
	  @ExceptionHandler(RegistroJaExisteException.class)	  
	  @ResponseStatus(HttpStatus.CONFLICT)
	  String registroJaExisteException(RegistroJaExisteException ex) {
//		  System.out.println("ErroException -> registroJaExisteException");
//		  BindingResult result = ex.getBindingResult();
//		  List<FieldError> fieldErrors = result.getFieldErrors();
//		  return processFieldErrors(fieldErrors)	  
	    return ex.getMessage();
	  }

	  @ExceptionHandler(RegistroNaoExisteException.class)	  
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  String registroNaoExisteException(RegistroNaoExisteException ex) {
		  System.out.println("ErroException -> registroNaoExisteException");
		  
	    return ex.getMessage();
	  }
	  
	  @ExceptionHandler(NegocioException.class)	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  String negocioException(NegocioException ex) {
	    return ex.getMessage();
	  }
	  
	  @ExceptionHandler(MethodArgumentNotValidException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST) 
	   Error  methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		  System.out.println("ErroException -> MethodArgumentNotValidException");
		  BindingResult result = ex.getBindingResult();
		  List<FieldError> fieldErrors = result.getFieldErrors();
		  return processFieldErrors(fieldErrors);
		  // return ex.getMessage();
/*	        BindingResult result = ex.getBindingResult();
	        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
	        return processFieldErrors(fieldErrors);
*/	        
	    }
	

	    private Error processFieldErrors(List<FieldError> fieldErrors) {
	        Error error = new Error(Instant.now(),  HttpStatus.BAD_REQUEST.value(), "Erro de validação");
	        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
	            error.addFieldError(fieldError.getObjectName(),  fieldError.getField(), fieldError.getDefaultMessage());
	        }
	        System.out.println(error);
	        return error;
	    }

	    static class Error {
	    

			private final int status;
	        private final String message;
	        private Instant timestamp;
	        private List<FieldError> fieldErrors = new ArrayList<>();

	        Error(Instant timestamp, int status, String message) {
	        	this.timestamp = timestamp; 
	            this.status = status;
	            this.message = message;
	            System.out.println(this.timestamp);
	        }

	        public int getStatus() {
	            return status;
	        }

	        public String getMessage() {
	            return message;
	        }

	        public void addFieldError(String object, String path, String message) {
	            FieldError error = new FieldError(object,path, message);
	            fieldErrors.add(error);
	        }

	        public List<FieldError> getFieldErrors() {
	            return fieldErrors;
	        }
	        @Override
	    	public String toString() {
	    				return "Error [status=" + status + ", message=" + message + ", data=" + timestamp + ", fieldErrors="
	    						+ fieldErrors + "]";
	    	}
	    }
}
