package jupiterpa.template.domain.service;

import org.slf4j.*;

import jupiterpa.template.domain.model.*;

public class TemplateTransformer {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private static final Logger logger = LoggerFactory.getLogger(new TemplateTransformer().getClass());
	
	public static TemplateEntity transform(TemplateEntity entity) {
		logger.info(TECHNICAL,"Entity {} should be transformed", entity);
		TemplateEntity newEntity = new TemplateEntity(entity.getValue()+"T");
		logger.info(TECHNICAL,"Entity {} was transformed to {}", entity, newEntity);
		return newEntity;
	}
}
