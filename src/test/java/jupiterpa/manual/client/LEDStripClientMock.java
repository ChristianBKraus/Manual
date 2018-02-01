package jupiterpa.manual.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jupiterpa.manual.domain.client.LEDStripClient;
import jupiterpa.manual.domain.model.LED;

@Component
@Profile("mock")
public class LEDStripClientMock implements LEDStripClient, ClientMocking {
	
	List<LED> leds = new ArrayList<LED>();

	// Mocking
	@SuppressWarnings("unchecked")
	public void inject(Object result) {
		if (result != null)
		  leds = (List<LED>) result;
	}
	public Object getState() {
		return leds;
	}

	@Override
	public boolean set(LED led) {
		leds.remove(led);
		leds.add(led);
		return true;
	}
	@Override
	public boolean defaultSet(LED led) {
		// TODO Auto-generated method stub
		return false;
	}

}
