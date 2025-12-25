package in.sp.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import in.sp.main.entity.JournalEntry;
import in.sp.main.entity.User;
import in.sp.main.repository.JournalRepo;
import in.sp.main.repository.UserRepo;

@ExtendWith(MockitoExtension.class) // this will enable use Mockito based annotations
public class JournalAppServiceImplTest {
	@Mock
	private JournalRepo journalRepo; // creating a mock JournalRepo interface bean
	
	@Mock
	private UserRepo userRepo; // creating a mock UserRepo interface bean
	
	@Mock
	private User user; // creating a mock User class bean
	
	@InjectMocks
	private JournalAppServiceImpl impl; // injecting the mock in JournalAppServiceImpl bean
	
	@ParameterizedTest
	@ValueSource(strings = {"Manash"})
	@Disabled
	public void getJournalsByUsernameTest(String username) {
		 
		 
		 List<JournalEntry> mockEntries = new ArrayList<>();
		 JournalEntry journalEntry1 = JournalEntry.builder().title("monday").content("chest and bi").build();
		 JournalEntry journalEntry2 = JournalEntry.builder().title("tuesday").content("back and tri").build();
		 mockEntries.add(journalEntry1);
		 mockEntries.add(journalEntry2);
		 
		 when(userRepo.findByUserName(username)).thenReturn(User.builder().userName(username).password("12345").journalEntries(mockEntries).build());
		 
		 
		 List<JournalEntry> entries = impl.getJournalsByUsername(username);
		 
		 List<JournalEntry> expectedEntries = new ArrayList<>();
		 JournalEntry testJournalEntry1 = JournalEntry.builder().title("monday").content("chest and bi").build();
		 JournalEntry testJournalEntry2 = JournalEntry.builder().title("tuesday").content("back and tri").build();
		 expectedEntries.add(journalEntry1);
		 expectedEntries.add(journalEntry2);
		 
		 assertEquals(expectedEntries, impl.getJournalsByUsername(username), "The two lists should be equal");
		 
	}

}
