package br.com.fenix.dominio.uuid;

import org.hibernate.annotations.IdGeneratorType;
import java.lang.annotation.*;

@IdGeneratorType(UuidV7Generator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface GeneratedUuidV7 {
}