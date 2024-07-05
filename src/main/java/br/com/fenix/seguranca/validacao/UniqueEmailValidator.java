package br.com.fenix.seguranca.validacao;



import org.springframework.stereotype.Component;

import br.com.fenix.seguranca.usuario.UsuarioRepositorio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class UniqueEmailValidator
	implements ConstraintValidator<UniqueEmail, String> {
	
	private UsuarioRepositorio userRepository;
	
	public UniqueEmailValidator(UsuarioRepositorio userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void initialize(UniqueEmail arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext arg1) {
		
		return !userRepository.findByEmail(email).isPresent();
	}
}
