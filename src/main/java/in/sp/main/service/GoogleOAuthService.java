package in.sp.main.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;
import in.sp.main.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Service
@Slf4j
public class GoogleOAuthService {
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String client_id;
	
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String client_secret;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	public String getJwtAfterProcessingOAuth2Code(String code) {
		
		String jwtToken = null;
		
		// Step 1 : We send a POST request to the authorization server requesting the token in exchange of the code
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // we will be sending it via HTTP POST
		params.add("code", code);
		params.add("redirect_uri", "https://developers.google.com/oauthplayground"); 
		params.add("grant_type", "authorization_code");
		params.add("client_id", client_id);
		params.add("client_secret", client_secret);
		
		String googleAuthorizationServerUrl = "https://oauth2.googleapis.com/token"; // this is the authorization server end point
                                                                            		 // This end point is used by clients to exchange an authorization code, 
                                                                                     //	a refresh token, or other grant types for an access token 
                                                                                     // (and optionally a refresh token). In other words, it’s the end point you
                                                                                     // call when you need to obtain or refresh tokens after the initial authorization step.

		
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // for x-www-form-urlencoded
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		
		// Step 2 : We recieve the access token here
		ResponseEntity<Map> googleServerResponse = null;
	    
		
		try {
		    googleServerResponse = restTemplate.postForEntity(googleAuthorizationServerUrl, entity, Map.class); // for POST request
		} catch (HttpClientErrorException e) {
			log.error("Error :" + e.getResponseBodyAsString());
			throw e;
		}
		
		String idToken = (String)googleServerResponse.getBody().get("id_token");
		
		// Step 3 : Validate the id_token (this is not the access token, the googleServerResponse also has the access_token)
		String validateTokenUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;  // in this endpoint we validate the token (check documentation)
		
		ResponseEntity<Map> tokenInfo = restTemplate.getForEntity(validateTokenUrl, Map.class); // for GET request
		
		if (tokenInfo.getStatusCode() == HttpStatus.OK) {
			String userEmail = (String)tokenInfo.getBody().get("email");
			UserDetails userDetails = null;
			
			
			try {
				userDetails = userDetailsServiceImpl.loadUserByUsername(userEmail); // We will get error as initially there in no user with email
				                                                                    // as their username. Thus, the catch block will execute
				                                                                    // Throws UsernameNotFoundException exception
				
			} catch (Exception e) { // enter the user in the database
				
				User user = User.builder()
						.userName(userEmail)
						.password(passwordEncoder.encode(UUID.randomUUID().toString())) // we set a random password
						.email(userEmail)
						.roles(List.of("USER"))
						.build();
				
				userRepo.save(user);
			}
			// Provide the JWT token
			jwtToken = jwtUtil.generateToken(userEmail); // the subject of the token will contain the email as username
		}
			
		return jwtToken;
	}

}
