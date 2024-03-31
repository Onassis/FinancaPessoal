package br.com.fenix.seguranca.email;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {
	
	@Bean
	@ConditionalOnProperty(name="spring.mail.host",
			havingValue="foo", matchIfMissing=true)
	public MailSender mockMailSender() {
		
		return new MockMailSender();
	}
	
	@Bean
	@ConditionalOnProperty("spring.mail.host")
	public SmtpMailSender smtpMailSender(JavaMailSender javaMailSender) {
		
		return new SmtpMailSender(javaMailSender);
	}
}
