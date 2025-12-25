package in.sp.main.repository;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entity.User;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId>{
	
	// custom method
	@Query("{userName : '?0'}")
	public User findByUserName(String username);
	
	@Query("{email : '?0'}")
	public User findByEmail(String email);

}
