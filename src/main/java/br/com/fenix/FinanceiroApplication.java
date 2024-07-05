package br.com.fenix;

import java.util.TimeZone;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import br.com.fenix.seguranca.usuario.UsuarioServicoImp;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Servlet;



import java.util.EnumSet;

@SpringBootApplication
@ComponentScan("br.com.fenix.controller")
@ComponentScan("br.com.fenix.dominio.converter")
@EnableAutoConfiguration
@EnableCaching
public class FinanceiroApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC-3"));
	}
	


    
	public static void main(String[] args) {
		SpringApplication.run(FinanceiroApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("senha"));
	}

}
