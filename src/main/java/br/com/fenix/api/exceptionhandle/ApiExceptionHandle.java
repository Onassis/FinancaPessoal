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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
//@ControllerAdvice
public class ApiExceptionHandle extends ResponseEntityExceptionHandler {

//	@Autowired
//    private MessageSource messageSource; 
//
//	@ResponseBody
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<?> handleDBIntegrity(DataIntegrityViolationException ex, WebRequest request) {
//    	
//			Problema problema = new Problema(HttpStatus.CONFLICT, 
//    		    "Violação dos dados do banco",
//    			ex.getMessage(),
//				OffsetDateTime.now() );
//		System.out.println("ResponseEntityExceptionHandler	" );
//    	System.out.println(problema);
//        return new ResponseEntity<>(problema, HttpStatus.CONFLICT);
//    }   

/*	public static final String DEFAULT_ERROR_VIEW = "/error";

	  @ExceptionHandler(value = Exception.class)
	  public ModelAndView  defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
	    // If the exception is annotated with @ResponseStatus rethrow it and let
	    // the framework handle it - like the OrderNotFoundException example
	    // at the start of this post.
	    // AnnotationUtils is a Spring Framework utility class.
	    if (AnnotationUtils.findAnnotation
	                (ex.getClass(), ResponseStatus.class) != null)
	      throw ex;

	    Problema problema = new Problema(HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getMessage(),
				req.getRequestURL().toString(), 
				OffsetDateTime.now() );
	    // Otherwise setup and send the user to a default error-view.
	    
	    System.out.println(problema);
	    ModelAndView mav = new ModelAndView(req.getRequestURL().toString(),"problema",problema);
//	    mav.addObject("problema", problema);
//	    mav.addObject("exception", ex);
//	    mav.addObject("url", req.getRequestURL());
//	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	  }
*/	
/**	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
    	Problema problema = new Problema(HttpStatus.INTERNAL_SERVER_ERROR, 
				ex.getMessage(),
				OffsetDateTime.now() );
    	System.out.println(problema);
        return new ResponseEntity<>(problema, HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/    
/*	
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

