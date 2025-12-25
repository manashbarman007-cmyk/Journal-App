package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sp.main.model.SentimentData;

@Service
public class SentimentConsumerService {

	@Autowired
	private EmailService emailService;
	
	
	@KafkaListener(topics = "weekly-sentiments", groupId = "my-group")
	public void listeningConsumer(String message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			SentimentData sentimentData = mapper.readValue(message, SentimentData.class); //De-serializing
			
			// once this message is recieved we send the email asynchronously
			emailService.sendSimpleEmail(sentimentData.getEmail(), "Details on sentiment",
					"Most frequent sentiment in the last & days is : " + sentimentData.getSentiment());
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
	}
}
