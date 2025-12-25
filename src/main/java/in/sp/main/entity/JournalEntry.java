package in.sp.main.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import in.sp.main.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document(collection = "myJournalEntry") //we specified the name of the collection
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {
	
	@Id
	private ObjectId id; // in future program use "ObjectId" as data type
	
	@NonNull
	private String title;
	
	private String content;
	
	@NonNull
	private Sentiment sentiment;
	
	private LocalDateTime localDateTime;
	
}
