package in.sp.main.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import in.sp.main.cache.AppCache;
import in.sp.main.dto.UserDTO;
import in.sp.main.entity.User;
import in.sp.main.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

//Here, we will create all the "admin" end points (Eg: to get all users) 
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API", description = "api endpoints are accessible only to the admins")
public class AdminController {
	
	
	@Autowired
	private UserServiceImpl impl;
	
	@Autowired
	private AppCache appCache;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/all-users")
	@Operation(summary = "Get all users", tags = "Admin API")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
		
		List<User> list = impl.getAllUsers();
		
		List<UserDTO> list2 = list.stream().map(x -> modelMapper.map(x, UserDTO.class)).toList();
		
		return ResponseEntity.ok(list2);
		
	}
	
	@PostMapping("/create-new-admin")
	@Operation(summary = "Create an admin", tags = "Admin API")
	public ResponseEntity<User> createAdmin(@RequestBody User admin) {
		User administrator = impl.saveAdmin(admin);
		
		return (administrator != null) ? ResponseEntity.ok(administrator) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/clear-cache") // REASON FOR EXPOSINNG THIS END-POINT :-
	                            // Whenever we hit this API the init() method int AppCache class will be called and the hashmap will get re-initialized
	                            // with new "key" and "value" (It is useful if there is frequent changes in the key-value pair, using this API we won't 
	                            // have to restart the application again and again if there is a change in the key-value pair)
	@Operation(summary = "clear cache for weather api", tags = "Admin API")
	public void clearCache() {
		appCache.init();
	}

}
