package br.com.fenix.seguranca.util;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.com.fenix.seguranca.modelo.Usuario;



@SuppressWarnings("deprecation")
@Component
public class UtilSerguranca {
	
	private static MessageSource messageSource;
	
	public UtilSerguranca(MessageSource messageSource) {
		UtilSerguranca.messageSource = messageSource;
	}


	
	public static Optional<Usuario> currentUser() {
		
		Authentication auth = SecurityContextHolder
				.getContext().getAuthentication();
		
		if (auth != null) {
			Object principal = auth.getPrincipal();
			
			if (principal instanceof Usuario)
				return Optional.of((Usuario) principal);
		}
		
		return Optional.empty();
	}
	public static long userId() {
		Optional<Usuario> user = currentUser(); 
	    if  (user.isEmpty()) 
	    	return 0; 
	
	    	return user.get().getId();
    }
	    
	public static void login(UserDetails user) {
		
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	public static void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	public static void afterCommit(Runnable runnable) {
		
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronizationAdapter() {
				    @Override
				    public void afterCommit() {
				    	
						runnable.run();
				    }
			    }
			);	
	}
}
