package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.sp.main.cache.AppCache;
import in.sp.main.weatherresponse.WeatherResponse;

@Service
public class WeatherApiResponse {

	@Value("${weather.api.key}")
	private String apiAccessKey;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AppCache appCache;

	@Autowired 
	private RedisService redisService;

	private static String apiUrl;

	public WeatherResponse getWeather(String city) {
		apiUrl = appCache.cache.get("weather_api_key");

		WeatherResponse weatherFromRedis = redisService.getFromRedis(city, WeatherResponse.class);

		String finalApi = apiUrl.replace("<ApiKey>", apiAccessKey).replace("<CityName>", city); // these are some methods of String

		if (weatherFromRedis != null) { // we will provide the output from the cache (stored in Redis) and not hit the API
			return weatherFromRedis;
		}
		else { // as the data is not present in Redis we will have to hit the API
			ResponseEntity<WeatherResponse> responseEntity = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
			// It is an HTTP GET request and the JSON obtained will be 
			// de-serialized to the WeatherResponse POJO class.
			// We also provide the final API URl

			WeatherResponse body = responseEntity.getBody();  // we get the body of the response entity
			
			// we store it in Redis if body != null
			if(body != null) redisService.setInRedis(city, body, 300); // TTL is 300 seconds

			return body;
		}

	}
}
