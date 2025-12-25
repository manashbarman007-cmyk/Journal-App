package in.sp.main.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document(collection = "users") // the default name of the collection will be "user"
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
	
	@Id
	private ObjectId id;
	
	@Indexed(unique = true)
	@NonNull
	private String userName;
	
	@NonNull
	private String password; 
	
	private String city;
	
	@Indexed(unique = true)
	@NonNull
	private String email;
	
	private boolean sentimentAnalysis;
	
	private List<String> roles;
	
	@DBRef(lazy = true) // we link the User entity to the JournalEntry entity (Along with lazy loading). It references the JournalEntry entity
	private List<JournalEntry> journalEntries = new ArrayList<>();
	

}
