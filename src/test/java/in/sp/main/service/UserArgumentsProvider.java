package in.sp.main.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import in.sp.main.entity.User;


public class UserArgumentsProvider implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		
		User user1 = new User();
		user1.setUserName("abc");
		user1.setPassword("12345");
		
		User user2 = new User();
		user2.setUserName("xyz");
		user2.setPassword("12345");
	
		return Stream.of(
				Arguments.of(user1),
				Arguments.of(user2)
				);
				
	}

}
