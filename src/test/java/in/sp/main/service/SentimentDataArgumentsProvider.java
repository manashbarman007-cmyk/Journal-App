package in.sp.main.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import in.sp.main.enums.Sentiment;
import in.sp.main.model.SentimentData;

public class SentimentDataArgumentsProvider implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		SentimentData sentimentData = SentimentData.builder()
                .email("skratos055@gmail.com")
                .sentiment("Most frequent sentiment for last & days : " + Sentiment.HAPPY)
                .build();
		return Stream.of(Arguments.of(sentimentData));
	}

}
