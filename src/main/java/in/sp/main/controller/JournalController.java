package in.sp.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import in.sp.main.entity.JournalEntry;
import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;
import in.sp.main.service.JournalAppServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController // @ResposnseBody + @Controller
@RequestMapping("/journals") // this controller will handle all API requests with end point "/journals" 
@Tag(name = "Journal API", description = "CRUD operations in journals for authenticated user")
public class JournalController {
	
	@Autowired
	private JournalAppServiceImpl journalImpl;
	
	@Autowired
	private UserRepo userRepo;
	
	// we do not provide the username in the url unlike in previous case
	@GetMapping// here we will link journals for a particular user
	@Operation(summary = "Get journals of a user", tags = "Journal API")
	public ResponseEntity<List<JournalEntry>> getAllJournalsOfUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		User user = userRepo.findByUserName(username);
		
		
		List<JournalEntry> list = user.getJournalEntries(); // we will get the journal entries for the particular user
		
		return (!list.isEmpty()) ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
		
	}
	
	
	// We will implement these APIs in another program while doing authentication
	
	@GetMapping("/username") // here we will link journals for a particular user
	@Operation(summary = "Get journals of a user by username", tags = "Journal API")
	public ResponseEntity<List<JournalEntry>> getJournalsByUsername() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<JournalEntry> entries = journalImpl.getJournalsByUsername(username);
		
		return (entries != null) ? ResponseEntity.ok(entries) : ResponseEntity.notFound().build();
		
	}
	
	@GetMapping("/id/{journal_id}")
	@Operation(summary = "Get journal of a user by it's journal id", tags = {"Journal API"})
	public ResponseEntity<JournalEntry> getJournalOfUserWithJournalId(@PathVariable String journal_id) {
		
		ObjectId id = new ObjectId(journal_id);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<JournalEntry> entries = journalImpl.getJournalsByUsername(username);
		
		JournalEntry entry = entries.stream().filter(journal-> journal.getId().equals(id)).collect(Collectors.toList()).get(0);
		
		return (entry != null) ? ResponseEntity.ok(entry) : ResponseEntity.notFound().build();
	}
	
//	@GetMapping("/regex/{title}") // here we will link journals for a particular user
//	public ResponseEntity<List<JournalEntry>> getJournalOfUserByTitleRegex(@PathVariable String title) {
//		
//		List<JournalEntry> list = journalRepo.getByTitleRegex(title);
//		
//		boolean action = (!list.isEmpty()) ? true : false;
//		
//		return (action) ? ResponseEntity.ok(list) : ResponseEntity.notFound().build();
//		
//	}
	
	@PostMapping // here we will link journals for a particular user
	@Operation(summary = "Insert journal of a user", tags = {"Journal API"})
	public ResponseEntity<JournalEntry> setJournalsOfUser(@RequestBody JournalEntry journalEntry) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		
		JournalEntry journalEntry2 = journalImpl.insertJournalEntry(journalEntry, username);
		
		return (journalEntry2 != null) ? ResponseEntity.ok(journalEntry2) : ResponseEntity.notFound().build();
		
	}
	
	
	@PutMapping("/id/{journal_id}") // here we will link journals for a particular user
	@Operation(summary = "Update journal of a user by it's journal id", tags = {"Journal API"})
	public ResponseEntity<JournalEntry> updateJournalsOfUser(@RequestBody JournalEntry journalEntry, @PathVariable String journal_id) {
		ObjectId id = new ObjectId(journal_id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		JournalEntry journalEntry2 = journalImpl.updateJournalById(id, journalEntry, username);
		
		return (journalEntry2 != null) ? ResponseEntity.ok(journalEntry2) : ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/id/{journal_id}") // here we will link journals for a particular user
	@Operation(summary = "Delete journal of a user by it's journal id", tags = {"Journal API"})
	public ResponseEntity<String> deleteJournalOfUserByJournalId(@PathVariable String journal_id) {
		
		ObjectId id = new ObjectId(journal_id);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		boolean action = journalImpl.deleteJournalById(id, username);
		
		return (action) ? ResponseEntity.ok("Journal entry successfully deleted") : ResponseEntity.notFound().build();
	}


}