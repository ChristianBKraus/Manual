package jupiterpa.manual.intf.scheduler;

import java.util.ArrayList;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import jupiterpa.manual.domain.service.*;
import jupiterpa.infrastructure.client.HttpContext;
import jupiterpa.infrastructure.config.ApplicationConfig;

@Component
@Profile("default")
public class Scheduler {
	@Autowired
	ActionService service;
	@Autowired
	ApplicationConfig config;
	
	// Scheduled actions
	@Scheduled(cron = "0 0 1 * * SUN") // every SUN at 1 o'clock
	public void incrementTaschengeld() {
		setContext("Taschengeld");
		service.incrementTaschengeld();
		
	}
	@Scheduled(cron = "0 0 6 * * TUE") // every Tuesday at 6 o'clock 
	public void eislaufJonathan() {
		setContext("Eislauf Jonathan");
		service.eislaufJonathan(0); // 0= default
	}
	
	private void setContext(String name) {
		if (HttpContext.getCorrelationID() == "") {
			User user = new User(config.getName(), config.getUserPassword(), new ArrayList<GrantedAuthority>());
			HttpContext.setUser(user);
			HttpContext.determineCorrelationID("Scheduler/" + name);
			MDC.put("endpoint", "Scheduler - Update " + name);
		}
	}
}
