package in.sp.main.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	 
	
	public <T> T getFromRedis(String key, Class<T> entityClass){
		
		try {
			Object object = redisTemplate.opsForValue().get(key);
			
			ObjectMapper mapper = new ObjectMapper();
			
			//De-serialization (in Redis config, we have set the values stored in Redis to be of JSON format)
			//Thus, we have to convert from JSON to POJO ie de-serialization
	
			String jSon = mapper.writeValueAsString(object); // we convert the object to JSON string

			return (object != null) ? mapper.readValue(jSon, entityClass) : null;  // return the de-serialized output
		} catch (Exception e) {
			log.error("Exception : " + e);
			return null;
		}
	}
	
	
	public void setInRedis(String key, Object object, long ttl){ // ttl : Time To Live
		
		try {
			redisTemplate.opsForValue().set(key, object, ttl, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Exception : " + e);
		}
	}
}
