package jupiterpa.template.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepo extends MongoRepository<TemplateEntity,Long>{ }
