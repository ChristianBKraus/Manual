package jupiterpa.manual.domain.service;

import org.slf4j.*;

import jupiterpa.manual.domain.model.*;

public class LedStripTransformer {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private static final Logger logger = LoggerFactory.getLogger(new LedStripTransformer().getClass());
	
	public static LED transform(Action action) {
		logger.info(TECHNICAL,"Action {} with status {} should be transformed", action.getId(), action.getStatus());
		
		LEDLocation loc = null;
		switch (action.getId()) {
			case Action.HINWEIS_INES: loc = new LEDLocation(2,0); break;
			case Action.VERBOT_INES: loc = new LEDLocation(3,0); break;
			case Action.TASCHENGELD_INES: loc = new LEDLocation(4,0); break;
			case Action.HINWEIS_JONATHAN: loc = new LEDLocation(2,2); break;
			case Action.VERBOT_JONATHAN: loc = new LEDLocation(3,2); break;
			case Action.TASCHENGELD_JONATHAN: loc = new LEDLocation(4,2); break;
			default: return null; 
		};
		LEDColor color = null;
		switch (action.getStatus()) {
			case 0: color = LEDColor.Black; break;
			case 1: color = LEDColor.Yellow; break;
			default: color = LEDColor.Red;
		}
		LED led = new LED(loc,color);
		
		logger.info(TECHNICAL,"LED {} calculated from Action {}", led, action);
		
		return led;
	}
}
