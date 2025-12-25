package in.sp.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// The DTO (Data Transfer Object) contains only those properties that we want to transfer

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private String userName;


	private String city;


	private String email;


	private boolean sentimentAnalysis;

}
