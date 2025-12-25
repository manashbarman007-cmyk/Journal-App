package in.sp.main.service;

import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import in.sp.main.entity.User;
import in.sp.main.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
//	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override // this method save it in the database without encoding the password, we will used this method will saving the user
	          // after entering the journals (we use this method so that our password does not get re-encoded)
	public void insertUser(User user) {
		userRepo.save(user);
	}
	
	
	@Override // this method will encode the password and then save it in the database
	public User insertUserWithEncodedPassword(User user) {
		User newUser = null;

		try {
			user.setPassword(passwordEncoder.encode(user.getPassword())); // encrypt the raw password before saving in the database
			user.setRoles(Arrays.asList("USER"));
			newUser = userRepo.save(user);

		} catch (Exception e) {
			log.error("Error occurred for {}", user.getUserName(), e); // {} acts as a placeholder, user.getUserName() will be inserted there
		}

		return newUser;
	}
	

	
	@Override // this method will encode the password and then save it in the database as an ADMIN
	public User saveAdmin(User admin) {
		User newAdmin = null;

		try {
			admin.setPassword(passwordEncoder.encode(admin.getPassword())); // encrypt the raw password before saving in the database
			admin.setRoles(Arrays.asList("USER", "ADMIN"));
			newAdmin = userRepo.save(admin);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return newAdmin;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = userRepo.findAll();

		return users;
	}

	@Override
	public User getUserById(ObjectId userId) {
		User user = userRepo.findById(userId).orElse(null);

		return user;
	}

	@Override
	public User updateUserByUsername(String username, User updatedUser) {
		
		User userInDb = userRepo.findByUserName(username);
		
		User newUser = null;

		if (userInDb != null) {
			userInDb.setUserName(updatedUser.getUserName());
			userInDb.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // encrypt the raw password before saving in the database
			// = userRepo.save(updatedUser);
			newUser = userRepo.save(userInDb);

		} else {

			throw new IllegalArgumentException("No user found with usermame : " + username);

		}

		return newUser;
	}

	@Override
	public boolean deleteUserByUsername(String Username) {
		
		User user = userRepo.findByUserName(Username);
		
		boolean action = false;

		try {
			userRepo.deleteById(user.getId());
			action = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return action;
	}


	@Override
	public List<User> findUsersWithSentimentAnalysis() {
		
		Criteria criteria = new Criteria();
		
		criteria.andOperator(Criteria.where("sentimentAnalysis").is(true),
				             Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
		// Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") checks if the email is of valid format (Google it)
		
		Query query = new Query();
		query.addCriteria(criteria);
		
		List<User> list = mongoTemplate.find(query, User.class);
		
		return list;
	}
}
