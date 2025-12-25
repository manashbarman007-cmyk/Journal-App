package in.sp.main.weatherresponse;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


//It is the class used for deserialization (ie to convert JSON to POJO)
@Data
@NoArgsConstructor
public class WeatherResponse{
	 
	 private Current current;
	 
	 @Data // inner class
	 @NoArgsConstructor
	 public static class Current{
		 
		 @JsonProperty("observation_time")
		 private String observationTime;
		 
		 private int temperature;
		 
		 @JsonProperty("weather_code")
		 private int weatherCode;
		 
		 @JsonProperty("weather_descriptions")
		 private List<String> weatherDescriptions;
		
		 private int feelslike;
		
		}
	}










