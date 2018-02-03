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
				logger.info(TECHNICAL, "Reset on Action {}", action);
			    action.reset();
			    break;
			case 1:
  			    logger.info(TECHNICAL, "Trigger on Action {}", action);
			    action.trigger(1);
			    break;
			case -1:
  			    logger.info(TECHNICAL, "Untrigger on Action {}", action);
			    action.trigger(-1);
			    break;
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
		schedule(new ActionChange(Action.HINWEIS_JONATHAN, -10, this), duration ); // of 3 hours later
		logger.info(TECHNICAL, "Setting Eislauf Jonathan done");
		
	}
	
	private class ActionChange implements Runnable {
		String action;
		int    value;
		ActionService service;
		public ActionChange(String action, int value, ActionService service) {
			this.action = action;
			this.value = value;
			this.service = service;
		}
		@Override
		public void run() {
			service.action(action, value);
		}
		
	}
	private void schedule(ActionChange task, int delay) {
		SimpleAsyncTaskExecutor scheduler = new SimpleAsyncTaskExecutor();
		scheduler.execute(task,  delay );
	}
	
}
