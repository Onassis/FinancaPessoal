package br.com.fenix.fi.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class LongToCategoriaConverter implements Converter<Long, Categoria> {
	
	@Autowired
	private CategoriaServico service;

	@Override
	public Categoria convert(Long text) {
		if ( text == null)  {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id).orElseThrow();
	}
}
