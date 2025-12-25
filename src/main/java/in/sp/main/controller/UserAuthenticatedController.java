package in.sp.main.controller;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.dto.UserDTO;
import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;
import in.sp.main.service.UserServiceImpl;
import in.sp.main.service.WeatherApiResponse;
import in.sp.main.weatherresponse.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


//Here, we will create all the authenticated end points for user(Eg: User update and such) 
@RestController
@RequestMapping("/users")
@Tag(name = "Authenticated User API", description = "Only authenticated user can access this api")
public class UserAuthenticatedController {
	
	
	private UserServiceImpl impl;
	
	private ModelMapper modelMapper;
	
	private UserRepo repo;
	
	private WeatherApiResponse apiResponse;
	
	public UserAuthenticatedController(UserServiceImpl impl, UserRepo repo, WeatherApiResponse apiResponse, ModelMapper modelMapper) {
		super();
		this.impl = impl;
		this.repo = repo;
		this.apiResponse = apiResponse;
		this.modelMapper = modelMapper;
	}	
	
	@PutMapping
	@Operation(summary = "Update a user", tags = "Authenticated User API")
	public ResponseEntity<UserDTO> updateUser(@RequestBody User updatedUser) {
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); // this gives us the user name of the currently authenticated user
		
		User user = impl.updateUserByUsername(username, updatedUser);
		
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		
		return (userDTO != null) ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping
	@Operation(summary = "Delete a user", tags = "Authenticated User API")
	public ResponseEntity<String> deleteUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		boolean action = impl.deleteUserByUsername(username);
		
		return (action) ? ResponseEntity.ok("User successfully deleted") : ResponseEntity.notFound().build();
	}
	
	// Creating a GET request for the external Weather Api
	@GetMapping("/weather")
	@Operation(summary = "Get weather report for a user", tags = "Authenticated User API")
	public ResponseEntity<String> getWeatherFromApi() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		User user = repo.findByUserName(username);
		String city = user.getCity();
		
		WeatherResponse weatherResponse = apiResponse.getWeather(city);
		
		String returnString = "Hello " + username + ". Today's temperature is : " + weatherResponse.getCurrent().getTemperature()
				           + "°C and it feels like : " + weatherResponse.getCurrent().getFeelslike() + "°C";
		
		return (weatherResponse != null) ? ResponseEntity.ok(returnString) : ResponseEntity.notFound().build();
		
	}

}
