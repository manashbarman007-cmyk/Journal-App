package in.sp.main.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import in.sp.main.entity.ConfigAppEntity;

@Repository
public interface ConfigAppRepo extends MongoRepository<ConfigAppEntity, ObjectId>{

}
