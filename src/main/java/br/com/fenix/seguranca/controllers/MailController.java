package br.com.fenix.seguranca.controllers;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fenix.seguranca.email.MailSender;
import br.com.fenix.seguranca.email.MessagingException;



@RestController
public class MailController {
	
	private MailSender mailSender;

	public MailController(MailSender smtp) {
		this.mailSender = smtp;
	}

	@RequestMapping("/mail")
	public String mail() throws MessagingException {
		
		mailSender.send("trupti.green@gmail.com", "A test mail", "Body of the test mail");
		
		return "Mail sent";
	}
}
