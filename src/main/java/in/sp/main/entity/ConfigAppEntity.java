package in.sp.main.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "config_journal_entity")
@Data
@NoArgsConstructor
public class ConfigAppEntity {
	@Id
	private ObjectId id;
	
	private String key;
	
	private String value;

}
