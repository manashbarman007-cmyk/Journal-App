package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleEmail(String toEmail, String subject, String body) {
	
		try {
			SimpleMailMessage message = new SimpleMailMessage();
//			message.setFrom("manashbarman007@gmail.com");
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(body);
			
			mailSender.send(message);
		} catch (Exception e) {
			log.error("Could not send the email", e);
		}
	}

}
