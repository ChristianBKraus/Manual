package jupiterpa.manual.domain.client;

import jupiterpa.manual.domain.model.*;

public interface LEDStripClient {
	public boolean set(LED led);
	public boolean defaultSet(LED led);
}
