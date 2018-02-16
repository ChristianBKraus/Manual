package jupiterpa.manual;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import jupiterpa.infrastructure.actuator.HealthCheck;
import jupiterpa.infrastructure.actuator.InterfaceHealth;
import jupiterpa.manual.domain.client.LEDStripClient;
import jupiterpa.manual.domain.model.Action;
import jupiterpa.manual.domain.model.ActionRepo;
import jupiterpa.manual.domain.model.LED;
import jupiterpa.manual.domain.model.LEDColor;
import jupiterpa.manual.domain.model.LEDLocation;
import jupiterpa.manual.domain.service.ActionService;
import jupiterpa.manual.domain.service.LedStripTransformer;
import jupiterpa.manual.mock.ClientMocking;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"mock"})
public class ServiceTest {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private static final Logger logger = LoggerFactory.getLogger(new LedStripTransformer().getClass());
	
	@Autowired ActionRepo repo;
	@Autowired ActionService service;
	@Autowired LEDStripClient client;
	@Autowired InterfaceHealth health;
	
	
	@Before
	public void clearDB() {
		repo.deleteAll();
		ClientMocking mock = (ClientMocking) client;
		mock.inject(new ArrayList<LED>());
	}
	
	@Test
	public void straightForward() {
		logger.info(TECHNICAL,"StraightForward started");
		// Generic Action +1
		Action action1 = service.action("Action", 1);
		
		// There is one action in repo
		List<Action> actions = repo.findAll();
		assertEquals( actions.size(), 1 );
		Action action2 = repo.findAll().get(0);
		
		// This action is equal to returned one
		assertEquals( action1.toString(), action2.toString() );
		// and with the correct attributes
		assertEquals( action1.getId(), "Action");
		assertEquals( action1.getStatus(), 1);
		logger.info(TECHNICAL,"StraightForward ended");
	}
	
	private Action action(String id, int inc) {
		Action action1 = service.action(id, inc);
		Action action2 = repo.findById(id);
		assertNotNull(action1);
		assertNotNull(action2);
		assertEquals(action1.toString(),action2.toString());
		assertEquals( action1.getId(), id);
		return action1;
	}
	private List<LED> getLEDs() {
		ClientMocking mock = (ClientMocking) client;
		@SuppressWarnings("unchecked")
		List<LED> leds = (List<LED>) mock.getState();
		return leds;
	}
	private Health getHealth() {
		HealthCheck check = (HealthCheck) health;
		Health h = check.health();
		return h;
	}
	@Test
	public void triggerOnce() {
		logger.info(TECHNICAL,"TriggerOnce started");
		Action action = action("Action",1);
		assertEquals( action.getStatus(), 1);
		assertEquals( getLEDs().size(), 0); // no valid action
		assertEquals( "DOWN", getHealth().getStatus().getCode() );
		logger.info(TECHNICAL,"TriggerOnce ended");
	}
	@Test
	public void triggerTwice() {
		logger.info(TECHNICAL,"TriggerTwice started");
		action("Action",1);
		Action action = action("Action",1);

		assertEquals( action.getStatus(), 2);
		assertEquals( 0, getLEDs().size() ); // no valid action
		assertEquals( "DOWN", getHealth().getStatus().getCode() );
		logger.info(TECHNICAL,"TriggerTwice ended");
	}
	@Test
	public void triggerAndReset() {
		logger.info(TECHNICAL,"TriggerAndReset started");
		action("Action",1);
		Action action = action("Action",0);

		assertEquals( action.getStatus(), 0);
		assertEquals( getLEDs().size(), 0); // no valid action
		assertEquals( "DOWN", getHealth().getStatus().getCode() );
		logger.info(TECHNICAL,"TriggerAndReset ended");
	}
	@Test
	public void triggerValidAction() {
		logger.info(TECHNICAL,"TriggerValidAction started");
		LEDLocation loc = new LEDLocation(2,0);
		LEDColor color = LEDColor.Yellow;
		Action action = action(Action.HINWEIS_INES,1);

		assertEquals( action.getStatus(), 1);
		
		assertEquals( getLEDs().size(), 1); // no valid action
		LED led = getLEDs().get(0);
		assertEquals( loc.toString(), led.getLocation().toString());
		assertEquals( color.toString(), led.getColor().toString());

		assertEquals( "UP", getHealth().getStatus().getCode() );
		logger.info(TECHNICAL,"TriggerValidAction ended");
		
	}
	@Test
	public void incrementTaschengeld() {
		logger.info(TECHNICAL,"incrementTaschengeld started");
		LEDLocation loc_i = new LEDLocation(4,0);
		LEDLocation loc_j = new LEDLocation(4,2);
		LEDColor color = LEDColor.Yellow;
		
		service.incrementTaschengeld();
		
		Action action_i = repo.findById(Action.TASCHENGELD_INES);
		Action action_j = repo.findById(Action.TASCHENGELD_JONATHAN);
		assertNotNull(action_i);
		assertNotNull(action_j);
		assertEquals( action_i.getStatus(), 1);
		assertEquals( action_j.getStatus(), 1);
		
		assertEquals( getLEDs().size(), 2); // no valid action
		LED led_i = getLEDs().get(0);
		LED led_j = getLEDs().get(1);
		assertEquals( loc_i.toString(), led_i.getLocation().toString());
		assertEquals( color.toString(), led_i.getColor().toString());
		assertEquals( loc_j.toString(), led_j.getLocation().toString());
		assertEquals( color.toString(), led_j.getColor().toString());

		assertEquals( "UP", getHealth().getStatus().getCode() );
		logger.info(TECHNICAL,"incrementTaschengeld ended");
	}

}
