package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;

// we need this class for setting up authentication
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUserName(username); // this is the entity class
				    
		if (user != null) {
			UserDetails userDetails = org.springframework.security.core.userdetails.User.builder() // this "User" belongs to org.springframework.security.core.userdetails package
			                           .username(user.getUserName()) // provide the username of the current user
			                           .password(user.getPassword()) // provide the password of the current user			                          
			                           .roles(user.getRoles().toArray(new String[0])) // provide the roles of the current user
			                                                                      // instead of "new String[0]" we can also use
			                                                                      //"new String[user.getRoles().size()]" but the
			                                                                      // previous way is cleaner
			                        
			                           .build(); // build the "UserDetails" object
			
			
			return userDetails;
		}
		else {
			throw new UsernameNotFoundException("Username : " + username + ", not found");
		}
		
	}
	

}
