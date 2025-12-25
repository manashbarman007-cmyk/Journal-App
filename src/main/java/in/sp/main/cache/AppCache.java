package in.sp.main.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.sp.main.entity.ConfigAppEntity;
import in.sp.main.repository.ConfigAppRepo;
import jakarta.annotation.PostConstruct;

@Component
public class AppCache {
	
	public Map<String, String> cache; // we will store the API access key here
	
	@Autowired
	private ConfigAppRepo appRepo;
	
	
    @PostConstruct // this method will be invoked immediately after the AppCache bean is created and all of the dependencies of AppCache
                   // are injected
	public void init() { // init : initialize
    	cache = new HashMap<>();
		List<ConfigAppEntity> list = appRepo.findAll();
		for (ConfigAppEntity configAppEntity : list) {
			cache.put(configAppEntity.getKey(), configAppEntity.getValue());
		}
	}
}
