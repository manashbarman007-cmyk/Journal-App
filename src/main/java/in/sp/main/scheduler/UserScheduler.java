package in.sp.main.scheduler;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.sp.main.entity.JournalEntry;
import in.sp.main.entity.User;
import in.sp.main.enums.Sentiment;
import in.sp.main.model.SentimentData;
import in.sp.main.service.EmailService;
import in.sp.main.service.UserServiceImpl;

@Component
public class UserScheduler {

//	private EmailService emailService;

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private EmailService emailService;


	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	// We will call the EmailService asynchronously using Kafka
	@Scheduled(cron = "0 0 9 * * SUN")
	public void scheduleJournalContentsOfUserWithSa() { // Sa: Sentinent Analysis

		List<User> users = userServiceImpl.findUsersWithSentimentAnalysis();

		// retrieve the journal entries for each user
		for(User user : users) {
			List<JournalEntry> jEntries = user.getJournalEntries();

			// we filter out the sentiments of each user for the last 7 days and put it in a list
			List<Sentiment> sentimentsList = jEntries.stream().filter(entry -> entry.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(7)))
					.map(x -> x.getSentiment()).toList();

			int n = sentimentsList.size();

			// Store the Sentiment and Integer in a map
			Map<Sentiment, Integer> mapForSentimentCount = new HashMap<>();

			for (int i = 0; i < n; i++) {
				Sentiment key = sentimentsList.get(i);
				int value = mapForSentimentCount.get(key);
				if (!mapForSentimentCount.containsKey(key)) {
					mapForSentimentCount.put(key, value + 1);
				}else { // simply add the count
					mapForSentimentCount.put(key, value + 1);
				}
			}
			// our goal next is to find the highest occurring sentiment in the last 7 days and we will send it to the user via email
			int maxCount = 0;
			Sentiment maxSentiment = null;

			for (Map.Entry<Sentiment, Integer> entry : mapForSentimentCount.entrySet()) {

				Sentiment key = entry.getKey();
				int val = entry.getValue();
				if (maxCount <= val) {
					maxCount = val;
					maxSentiment = key;					
				}

			}

			// Send the email to the user
			if (maxSentiment != null) {
				
				SentimentData sentimentData = SentimentData.builder()
						.email(user.getEmail())
						.sentiment("Most frequent sentiment for the last 7 days : " + maxSentiment)
						.build();
				ObjectMapper mapper = new ObjectMapper();

				try { // Asynchronously calling
					String json = mapper.writeValueAsString(sentimentData);
					kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), json); 
				} catch (JsonProcessingException e) { // Synchronously calling
					emailService.sendSimpleEmail(user.getEmail(), "Details on sentiment", "Most frequent sentiment for the last 7 days : " + maxSentiment);
					e.printStackTrace();

				}
			}
		}
	}
}


