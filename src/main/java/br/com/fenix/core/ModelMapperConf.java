package br.com.fenix.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Configura uma classe de terceiros como servico spring
@Configuration
public class ModelMapperConf {
	@Bean
    public ModelMapper modelMapper() {
    	 return new ModelMapper();
    }

    
}
