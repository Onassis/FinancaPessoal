package br.com.fenix.dominio.converter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.fenix.dominio.modelo.DadoBasico.Moeda;
import br.com.fenix.dominio.repositorio.dadosBasico.MoedaRepositorio;

@Component
public class StringToMoedaConverter implements Converter<String,Moeda> {

	@Autowired
	MoedaRepositorio moedaRp;
	
	@Override
	public Moeda convert(String source) {
		
		if (source.isEmpty()) {
			return null;
		}
		Optional<Moeda> moedaOp = moedaRp.findByCodigo(source);
		if (moedaOp.isEmpty()) {
			return null;
		}
			
		return moedaOp.get();
	}
}
