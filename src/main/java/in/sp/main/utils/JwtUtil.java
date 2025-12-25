package in.sp.main.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final String SECTRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V"; // must be of 256 bit (just google
	                                                                       // "JWT secret key generator")

	// we create the token
	public String createToken(Map<String, Object> claims, String subject) { // subject is the user name
	
		String token = Jwts.builder()
				       .claims(claims) // - Adds custom claims (extra data) to the payload section of the token
				       .subject(subject) //contains the username
				       .header().empty().add("typ", "JWT") // this is contained in the Header section of the token
				                                           // .empty() clears the default header before adding "typ", "JWT"
				       .and() // connects another JwtsBuilder
				       .issuedAt(new Date(System.currentTimeMillis())) // the time when the token is created
				       .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 50))) // the expiration time is 50 minutes
				       .signWith(getSigningKey())
				       .compact();	// - Finalizes the token and returns it as a compact string
	       
				 
		return token;
	}
 
	// provides the secret key for signing
	public SecretKey getSigningKey() {

		byte[] bytes = SECTRET_KEY.getBytes(); // converts the String to a byte[] array
		return Keys.hmacShaKeyFor(bytes); // creates a key suitable for signing JWTs
	}
	
	// Finally, we generate the token
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		
		// insert whatever user related informations you want in the map (For our application we have no such needs. So, we keep it empty)
		/// You can add user roles, permissions, or other metadata here
		return createToken(claims, username);
	}
	
	
	// Extract all claims
	public Claims extactAllClaims(String token) {
		return Jwts.parser() // Starts building the JwtParser, this parser will be configured to verify and decode the token
			   .verifyWith(getSigningKey()) // Configure's the parser to verify the token's signature. We pass the SECRET_KEY which was used
			                                // to build the original token. This ensures that the token hasn't been tampered with
			   .build() // returns a JwtParser
			   .parseSignedClaims(token) // parses the JWT string and verifies the signature
			   .getPayload(); // extract the payload (claims) from the parsed claims. This includes fields like sub (subject, basically the username),
		                      // iat (issued at), exp (expiration) and other custom claims
	}
	
	
	// Extract username (subject) from claims
	public String extractUsername(String token) {
		Claims claims = extactAllClaims(token);
		
		return claims.getSubject();
	}
	
	// Extract the expiration from the claims
	public Date extractExpiration(String token) {
		Claims claims = extactAllClaims(token);
		
		return claims.getExpiration();
	}
	
	// Check if the token is expired
	public boolean isTokenExpired(String token) {
		Date expirationDate = extractExpiration(token);
		
		return expirationDate.before(new Date()); // new Date() returns the current date and time
 	}

	// Check if the token is valid
	public boolean isTokenValid(String token) {
		return !isTokenExpired(token); // the username provided must be same as the username in the jwt
	}
}
