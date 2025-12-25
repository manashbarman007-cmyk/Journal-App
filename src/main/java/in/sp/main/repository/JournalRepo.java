package in.sp.main.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entity.JournalEntry;

@Repository
public interface JournalRepo extends MongoRepository<JournalEntry, ObjectId>{
	
	//custom query : find JournalEntry by using regex pattern in title
	@Query("{'title' : {$regex : ?0, $options : 'i'}}") // $options : 'i' is for case-insensitivity
	public List<JournalEntry> getByTitleRegex (String titlePattern);
}
