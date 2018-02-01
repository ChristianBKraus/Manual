package jupiterpa.template.domain.client;

import jupiterpa.template.domain.model.TemplateEntity;

public interface TemplateClient {
	public boolean set(TemplateEntity entity);
	public boolean defaultSet(TemplateEntity entity);
}
