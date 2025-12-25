package in.sp.main.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	@Disabled
	public void setPropertiesTest() {
		redisTemplate.opsForValue().set("name","Manash");
	}

}
