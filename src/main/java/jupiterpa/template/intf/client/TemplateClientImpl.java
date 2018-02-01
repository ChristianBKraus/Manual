package jupiterpa.template.intf.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import jupiterpa.infrastructure.client.ClientBase;

import jupiterpa.template.domain.client.TemplateClient;
import jupiterpa.template.domain.model.*;

@Component
@Profile("default")
public class TemplateClientImpl extends ClientBase<TemplateEntity> implements TemplateClient {

	@HystrixCommand(fallbackMethod = "defaultSet")
	public boolean set(TemplateEntity entity) {
		TemplateEntity newEntity = post("templateClient","/",entity);
		if (newEntity == null) 
			return false;
		else
			return true;
	}
	
	public boolean defaultSet(TemplateEntity entity) {
		warn("SET for Template failed");
		return false;
	}
}
