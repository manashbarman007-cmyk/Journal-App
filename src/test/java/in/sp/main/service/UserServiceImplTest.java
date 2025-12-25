package in.sp.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;

@SpringBootTest
public class UserServiceImplTest {
	@Autowired
	private UserServiceImpl impl;
	
	@Autowired
	private UserRepo repo;
	
	@Disabled
	@Test
	public void doTheTest() {
		
		User user = repo.findByUserName("manash");
		assertTrue(!user.getJournalEntries().isEmpty());
		
	}
	
	@Disabled
	@ParameterizedTest
	@CsvSource({
		"1, 2, 3",
		"2, 2, 4",
		"3, 4, 7"
	})
	public void doTestWithParamaters(int a, int b, int expected) {
		assertEquals(expected, a + b);
	}

	@Disabled
	@ParameterizedTest
	@ArgumentsSource(UserArgumentsProvider.class)
	public void testInsertUserWithEncodedPassword(User user) {
		assertNotNull(impl.insertUserWithEncodedPassword(user));
		
	}
	
	@ParameterizedTest
	@ArgumentsSource(UserArgumentsProvider.class)
	@Disabled
	public void testDeleteUserByUsername(User user) {
		assertTrue(impl.deleteUserByUsername(user.getUserName()));
	}
}
