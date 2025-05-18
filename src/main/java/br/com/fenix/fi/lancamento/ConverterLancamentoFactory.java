package br.com.fenix.fi.lancamento;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.fi.lancamento.operacao.CompraParceladaConverter;
import br.com.fenix.fi.lancamento.operacao.CreditoConverter;
import br.com.fenix.fi.lancamento.operacao.DebitoConverter;
import br.com.fenix.fi.lancamento.operacao.TransferenciaConverter;

import java.util.HashMap;
import org.springframework.stereotype.Component;
import br.com.fenix.abstrato.dto.Converter;


@Component
public class ConverterLancamentoFactory {
    
    private HashMap<String, Converter<?, ?> > converters = new HashMap<>();
    
    private HashMap<String, String> cadastro = new HashMap<>(); 
    
    public ConverterLancamentoFactory() {
        converters.put("CP", new CompraParceladaConverter());
        converters.put("CR", new CreditoConverter());
        converters.put("DB", new DebitoConverter());
        converters.put("TR", new TransferenciaConverter());
        
        cadastro.put("CP", "lancamento/cad_parcelado");
        cadastro.put("CR", "lancamento/cad_credito");
        cadastro.put("DB", "lancamento/cad_debito");
        cadastro.put("TR", "lancamento/cad_Transferencia");

    }
    
    public Converter<?,?> getConverter(String discriminatorValue) {
        return converters.get(discriminatorValue);
    }

	
    public String getCadastro(String discriminatorValue) {
        return cadastro.get(discriminatorValue);
    } 
}