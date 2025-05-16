package br.com.fenix.fi.lancamento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.fenix.abstrato.dto.Converter;
import jakarta.persistence.DiscriminatorValue;

//@Component
public class ConverterContext {
    private final Map<Class<?>, Converter<?, ?>> converters = new HashMap<>();


//    public static String getDiscriminatorValue(Class<?> clazz) {
//        if (clazz.isAnnotationPresent(DiscriminatorValue.class)) {
//            DiscriminatorValue discriminatorValue = clazz.getAnnotation((Class<?>) DiscriminatorValue.class);
//            return discriminatorValue.value();
//        }
//        return null;
//    }


    @Autowired
    public ConverterContext(List<Converter<?, ?>> converterList) {
        for (Converter<?, ?> converter : converterList) {
            converters.put(getEntityClass(converter), converter);
        }
    }

    private Class<?> getEntityClass(Converter<?, ?> converter) {
        // Implement logic to determine the entity class based on the converter instance
        if (converter instanceof CompraParceladaConverter) {
            return CompraParcelada.class;
        } else if (converter instanceof CreditoConverter) {
            return Credito.class;
        } else if (converter instanceof DebitoConverter) {
            return Debito.class;
        }
        throw new IllegalArgumentException("Unknown converter type: " + converter.getClass());
    }

    public <E, D> Converter<E, D> getConverter(Class<E> entityClass) {
        return (Converter<E, D>) converters.get(entityClass);
    }
}

