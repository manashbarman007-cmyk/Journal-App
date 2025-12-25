package in.sp.main.service;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import in.sp.main.model.SentimentData;
import in.sp.main.scheduler.UserScheduler;

@SpringBootTest
public class SentimentConsumerServiceTest {

	@Autowired
	private UserScheduler userScheduler;
	
	
//	@ParameterizedTest
//	@ArgumentsSource(SentimentDataArgumentsProvider.class)
//	public void listeningConsumerTest(SentimentData sentimentData) {
//		
//		userScheduler.scheduleJournalContentsOfUserWithSa(sentimentData);
//	}
}
