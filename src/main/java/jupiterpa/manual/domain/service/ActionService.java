package jupiterpa.manual.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jupiterpa.infrastructure.actuator.*;
import jupiterpa.manual.domain.client.*;
import jupiterpa.manual.domain.model.*;

@Service
public class ActionService {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private static final Logger logger = LoggerFactory.getLogger(new LedStripTransformer().getClass());

	@Autowired
	LEDStripClient client;
	@Autowired
	ActionRepo repo;
	@Autowired 
	InterfaceHealth health;

	public Action action(String name, boolean reset) {
		
		// Read from DB
		Action action = repo.findOne(name);
		if (action == null) {
			action = new Action(name,false);
		}
		
		// Trigger / Reset on Entity
		if (reset) {
			logger.info(TECHNICAL, "Reset on Action {}", action);
			action.reset();
		} else { 
			logger.info(TECHNICAL, "Trigger on Action {}", action);
			action.trigger();
		}
		// Save to DB
		repo.save(action);
		
		// Transform
		LED led = LedStripTransformer.transform(action);
		// Client Call
		boolean success = client.set(led);

		// Update Health
		if (success) 
			health.setHealth(new HealthInfo("TemplateClient",true,"running"));
		else 
			health.setHealth(new HealthInfo("TemplateClient",false,"down"));
		
		return action;
	}
}
