package br.com.fenix.dominio.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoConta;

@Component
public class StringToTipoCategoriaConverter implements Converter<String, TipoCategoria> {

    @Override
    public TipoCategoria convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        System.out.println(source);
        for (TipoCategoria tipo : TipoCategoria.values()) {
        	System.out.println(tipo.getTipoCategoria());
            if (tipo.getTipoCategoria().equalsIgnoreCase(source)) {
            	
                return tipo;
            }
        }
        throw new IllegalArgumentException("TipoConta inválido: " + source);
    }
}