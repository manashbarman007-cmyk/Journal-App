package in.sp.main.service;

import java.util.List;
import org.bson.types.ObjectId;
import in.sp.main.entity.JournalEntry;

public interface JournalAppService {

	//Crud Operations
	
	public JournalEntry insertJournalEntry(JournalEntry journalEntry, String username);
	
	public List<JournalEntry> getAllEntries();
	
	public List<JournalEntry> getJournalsByUsername(String username) ;
	
	public JournalEntry updateJournalById(ObjectId id, JournalEntry updatedEntry, String username);
	
	public boolean deleteJournalById(ObjectId id, String username);
}
