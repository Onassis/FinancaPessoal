package br.com.fenix.dominio.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.fenix.dominio.enumerado.TipoConta;

@Component
public class StringToTipoContaConverter implements Converter<String, TipoConta> {

    @Override
    public TipoConta convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        System.out.println(source);
        for (TipoConta tipoConta : TipoConta.values()) {
        	System.out.println(tipoConta.getTipoConta());
            if (tipoConta.getTipoConta().equalsIgnoreCase(source)) {
            	
                return tipoConta;
            }
        }
        throw new IllegalArgumentException("TipoConta inválido: " + source);
    }
}