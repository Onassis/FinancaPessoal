package br.com.fenix.email;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ConfigEmail {
/**    @Bean
    public MailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setUsername("onassis.tavares@gmail.com");
        mailSender.setPassword("Sheldom@13#");
        mailSender.setPort(587);

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smpt.starttls.enable", true);

        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }
**/    
}
