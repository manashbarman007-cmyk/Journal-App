package in.sp.main.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper configMapper() {
		return new ModelMapper();
	}

}
