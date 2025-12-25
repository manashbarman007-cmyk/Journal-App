package in.sp.main.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {
	
	@Autowired
	private EmailService service;
	
	@Test
	@Disabled
	public void sendEmail() {
		service.sendSimpleEmail("hulksmash202122@gmail.com", "Message", "Baap baap hota hai!");
	}

}
