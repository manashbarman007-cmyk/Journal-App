package in.sp.main.service;

import java.util.List;

import org.bson.types.ObjectId;

import in.sp.main.entity.User;

public interface UserService {
	
	//Crud Operations
	public void insertUser(User user);
	
    public User insertUserWithEncodedPassword(User user);
	
	public User saveAdmin(User admin);
	
	public List<User> getAllUsers();
	
	public User getUserById(ObjectId useId);
	
	public List<User> findUsersWithSentimentAnalysis();
	
	public User updateUserByUsername(String username, User updatedUser);
	
	public boolean deleteUserByUsername(String Username);

}
