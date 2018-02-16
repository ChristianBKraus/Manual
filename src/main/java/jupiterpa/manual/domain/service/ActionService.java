package jupiterpa.manual.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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
	@Autowired
	TaskExecutor executor;

// Taschengeld:
//  +: Scheduled at Sunday
//  -: Manual
//Verbot:
//  +: Manual
//  -2: One day later
//Hinweis:
//  +?: Scheduled (hard coded)
//  -: two hours later 
	

	public Action action(String name, int inc) {
		
		// Read from DB
		Action action = repo.findOne(name);
		if (action == null) {
			action = new Action(name,false);
		}
		
		// Trigger / Reset on Entity
		switch (inc) {
			case 0: 
				logger.info(TECHNICAL, "Reset on {}", action);
			    action.reset();
			    break;
			case 1:
  			    logger.info(TECHNICAL, "Trigger on {}", action);
			    action.trigger(1);
			    break;
			case -1:
  			    logger.info(TECHNICAL, "Untrigger on {}", action);
			    action.trigger(-1);
			    break;
		}
		// Save to DB
		repo.save(action);
		
		// Transform
		boolean success;
		LED led = LedStripTransformer.transform(action);
		if (led == null) {
			logger.warn(TECHNICAL, "Transformation of {} failed");
			success = false;
		} else {
		  // Client Call
		  success = client.set(led);
		}

		// Update Health
		if (success) 
			health.setHealth(new HealthInfo("TemplateClient",false,"running"));
		else 
			health.setHealth(new HealthInfo("TemplateClient",true,"down"));
		
		return action;
	}

	public void incrementTaschengeld() {
		logger.info(TECHNICAL, "Increment Taschengeld");
		
		action(Action.TASCHENGELD_INES, 1);
		action(Action.TASCHENGELD_JONATHAN, 1);
		
		logger.info(TECHNICAL, "Taschengeld incremented");
	}
	
	public void eislaufJonathan(int duration) {
		logger.info(TECHNICAL, "Setting Eislauf Jonathan");
		
		action(Action.HINWEIS_JONATHAN, 10);
		if (duration == 0)
			duration = 1000 * 60 * 60 * 3; // 3 Stunden
	    executor.schedule(new Task(Action.HINWEIS_JONATHAN, -10), duration ); // of 3 hours later
		logger.info(TECHNICAL, "Setting Eislauf Jonathan done");
		
	}
		
}
