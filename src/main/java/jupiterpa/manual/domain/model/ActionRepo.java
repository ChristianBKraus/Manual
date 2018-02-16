package jupiterpa.manual.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepo extends MongoRepository<Action,String>{ 
	public Action findById(String id);
}
