package jupiterpa.manual.intf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import jupiterpa.infrastructure.client.ClientBase;
import jupiterpa.manual.domain.client.LEDStripClient;
import jupiterpa.manual.domain.model.*;

@Component
@Profile("default")
public class LEDStripClientImpl extends ClientBase<LED> implements LEDStripClient {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@HystrixCommand(fallbackMethod = "defaultSet")
	public boolean set(LED led) {
		put("ledStrip","/ledstrip",led);
		return true;
	}
	
	public boolean defaultSet(LED led) {
		logger.warn(TECHNICAL, "SET LEDStrip failed");
		return false;
	}	
}
