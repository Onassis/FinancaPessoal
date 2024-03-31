package br.com.fenix.seguranca.validacao;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;





import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Constraint(validatedBy={})
@NotBlank
@Size(min=6, max=32)
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Password {

    String message() default "{duplicateEmail}";

    Class[] groups() default {};
    
    Class[] payload() default {};
}
