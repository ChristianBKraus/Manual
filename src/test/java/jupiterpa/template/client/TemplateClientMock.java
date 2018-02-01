package jupiterpa.template.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jupiterpa.template.domain.client.TemplateClient;
import jupiterpa.template.domain.model.TemplateEntity;

@Component
@Profile("mock")
public class TemplateClientMock implements TemplateClient, ClientMocking {
	
	List<TemplateEntity> entities = new ArrayList<TemplateEntity>();

	// Mocking
	@SuppressWarnings("unchecked")
	public void inject(Object result) {
		if (result != null)
		  entities = (List<TemplateEntity>) result;
	}
	public Object getState() {
		return entities;
	}

	// Interface
	public boolean set(TemplateEntity entity) {
		entities.add(entity);
		return true;
	}
	public boolean defaultSet(TemplateEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
