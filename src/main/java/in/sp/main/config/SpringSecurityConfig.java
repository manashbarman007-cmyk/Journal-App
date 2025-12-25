package in.sp.main.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.sp.main.filter.JwtFilter;
import in.sp.main.service.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtFilter jwtFilter;

	//1: provide the SecurityFilter Chain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http.authorizeHttpRequests((requests) -> requests // http.authorizeHttpRequests sets authorization rules for incoming Http requests 
				.requestMatchers("/public/**", "/swagger-ui/**",  "/v3/**", "/auth/**").permitAll() // anyone can access URLs starting with "/public/", "/swagger-ui/"
				                                                                        // "/v3/**" and "/auth/**" endpoints
			    .requestMatchers("/journals/**", "/users/**").authenticated() // only authenticated (ie verified users) can access URLs starting with
				                                                             // "/journals/**" and "/users/**"
				//.requestMatchers("/journals/**").authenticated() // only authenticated (ie verified users) can access URLs starting with
                                                                 // "/journals/**"
				.requestMatchers("/admin/**").hasRole("ADMIN") //Only users with the ADMIN role can access /admin/ paths

				.anyRequest().authenticated()) // any other requests need authentication
				//.httpBasic(Customizer.withDefaults()) // Enables HTTP Basic Authentication,
				                                      // where credentials are sent with each request (usually in the Authorization header)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				
				.csrf((csrf) -> csrf.disable()) // disables csrf protection (safe for stateless REST APIs)
				
				.build(); // -  builds the SecurityFilterChain bean that Spring uses to apply these rules
 
	}
	
	
	
	//2: configure the AuthenticationManagerBuilder (This builder is used to define how users are authenticated)
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(myPasswordEncoder());
	} 
	
	
	
	//3: encode the password
	@Bean
	public PasswordEncoder myPasswordEncoder() {
		return new BCryptPasswordEncoder(); // we return BCryptPasswordEncoder()
	}
	
	// Create a bean for AuthenticationManager (needed for JWT) 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}


//Extra knowledge: 


// To enable csrf: http.csrf(csrf -> csrf.withDefaults())


//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorize -> authorize
//                .anyRequest().authenticated() // All requests require authentication
//            )
//            .formLogin(form -> form
//                .loginPage("/login") // Specify custom login page
//                .permitAll() // Allow access to login page for all
//            )
//            .logout(logout -> logout
//                .logoutUrl("/perform_logout") // Custom logout URL (default is /logout)
//                .logoutSuccessUrl("/login?logout") // Redirect after successful logout
//                .invalidateHttpSession(true) // Invalidate HTTP session on logout (default is true)
//                .deleteCookies("JSESSIONID") // Delete specified cookies on logout
//                .permitAll() // Allow access to the logout URL for all
//            );
//        return http.build();
//    }
//}