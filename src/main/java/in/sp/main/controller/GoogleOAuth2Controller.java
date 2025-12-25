package in.sp.main.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import in.sp.main.service.GoogleOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth/google")
@Slf4j
@Tag(name = "Oauth2 API", description = "Login via Google")
public class GoogleOAuth2Controller {

	
	@Autowired
	private GoogleOAuthService service;

	@GetMapping("/callback")
	@Operation(summary = "Login via google", tags = "Oauth2 API")
	public ResponseEntity<?> oauthController(@RequestParam String code) {
		String jwtToken = null;
		try {
			jwtToken = service.getJwtAfterProcessingOAuth2Code(code);
			return ResponseEntity.ok(Map.of("token", jwtToken)); // key : token and value : jwtToken
		} catch (Exception e) {
			log.error("Exception occurred while handleGoogleCallback :" + e);
			e.printStackTrace();
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
