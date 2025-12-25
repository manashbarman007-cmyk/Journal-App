package in.sp.main.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.sp.main.entity.JournalEntry;
import in.sp.main.entity.User;
import in.sp.main.repository.JournalRepo;
import in.sp.main.repository.UserRepo;

@Service
public class JournalAppServiceImpl implements JournalAppService{
	
	@Autowired
	private JournalRepo journalRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserServiceImpl userImpl;

	@Transactional // no need to use try-catch-finally explicitly
	@Override
	public JournalEntry insertJournalEntry(JournalEntry journalEntry, String username) {
		
		journalEntry.setLocalDateTime(LocalDateTime.now());
		
		User user = userRepo.findByUserName(username);
			
		JournalEntry newJEntry = journalRepo.save(journalEntry);

		user.getJournalEntries().add(newJEntry); // we add the journal entry in the list 

		userImpl.insertUser(user); // we finally insert User in the mongoDB "users" collection (without password re-encoding)
					
		return newJEntry;
		
	}

	@Override
	public List<JournalEntry> getAllEntries() {
		
		List<JournalEntry> jEntries = journalRepo.findAll();
		
		return jEntries;
	}

	@Override
	public List<JournalEntry> getJournalsByUsername(String username) {
		
		User user = userRepo.findByUserName(username);
		
		List<JournalEntry> jEntries = user.getJournalEntries();
		
		return jEntries;	
		
	}

	
	@Override
	public JournalEntry updateJournalById(ObjectId id, JournalEntry updatedEntry, String username) {
		
		User user = userRepo.findByUserName(username);
		
		List<JournalEntry> journalEntries = user.getJournalEntries();
		
		JournalEntry jEntry = journalRepo.findById(id).orElse(null);
		
		JournalEntry newJEntry = null;
		
		
		if (jEntry != null) {
			
			// remove the old entry from journalEntries
			journalEntries.removeIf(entry -> entry.getId().equals(id));
			
			
			// update the jEntry
			jEntry.setLocalDateTime(LocalDateTime.now());

			jEntry.setTitle(updatedEntry.getTitle()); // set the updated title

			jEntry.setContent(updatedEntry.getContent()); // set the updated content

			newJEntry = journalRepo.save(jEntry); // save the updated entry in the database
			
			// we add the new entry in User object's List
			journalEntries.add(newJEntry);
			
			user.setJournalEntries(journalEntries); // we set the updated  journalEntries
			
			// Now save the updated user (without password re-encoding)
			userImpl.insertUser(user);
			
			
		} else {
			
			throw new IllegalArgumentException("No entry found with id : " + id);

		}	
				
		return newJEntry;
	}
	

	@Override
	public boolean deleteJournalById(ObjectId id, String username) {
		
		User user = userRepo.findByUserName(username);
		
		List<JournalEntry> journalEntries = user.getJournalEntries();
		
		
		boolean action = false;
		
		try {
			
			journalRepo.deleteById(id);
			
			List<JournalEntry> entries = getJournalsByUsername(username);
			
			// remove the old entry
			entries.removeIf((x) -> x.getId().equals(id)); // we put a predicate
			
			action = true;
			
			// Now save the updated user (without re-encoding the password)
			userImpl.insertUser(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return action;
		
	}

}
