package in.sp.main.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.dto.UserDTO;
import in.sp.main.entity.User;
import in.sp.main.service.UserDetailsServiceImpl;
import in.sp.main.service.UserServiceImpl;
import in.sp.main.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


// Here, we will create all the public end points (Eg: User Creation) 
@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public API", description = "Any user can access this api")
public class UserPublicController {
	
	@Autowired
	private UserServiceImpl impl;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/signup")
	@Operation(summary = "Sign in the user", tags = "Public API")
	public ResponseEntity<UserDTO> signup(@RequestBody User user) {
		
		User user2 = impl.insertUserWithEncodedPassword(user);
		
		UserDTO userDTO = modelMapper.map(user2, UserDTO.class);
		
		return (user2 != null) ? ResponseEntity.ok(userDTO) : ResponseEntity.noContent().build();
		
	}
	
	
	// When the user logs in we will provide a JWT token to the user
	@PostMapping("/login")
	@Operation(summary = "Log in the user", tags = "Public API")
	public ResponseEntity<String> login(@RequestBody User user) {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())); //authenticates the user
			                                                                                                        // by its username and raw password, the 
																													// raw password then gets encrypted and 
																													// then compared with the stored encrypted
																													// password in the database 
			UserDetails userByUsername = userDetailsServiceImpl.loadUserByUsername(user.getUserName());
			
			//Generate the JWT Token for the user
			String jwt = jwtUtil.generateToken(userByUsername.getUsername());
			return ResponseEntity.ok(jwt);
		} catch (Exception e) {
			log.error("Exception occurred while createAuthenticationToken ", e);
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
		}	
	}
	
	@GetMapping("/health_check")
	@Operation(summary = "Health check", tags = "Public API")
	public String healthCheck() {
		return "Good Health";
	}

}
