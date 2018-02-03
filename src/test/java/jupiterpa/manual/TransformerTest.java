package jupiterpa.manual;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import jupiterpa.manual.domain.model.*;
import jupiterpa.manual.domain.service.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"mock","test","standalone"})
public class TransformerTest { 
	@Test
    public void test() throws Exception {
    	Action action = new Action(Action.HINWEIS_INES,true);
    	LED led;
    	led = LedStripTransformer.transform(action);
    	assertEquals(LEDColor.Black.toString(),led.getColor().toString());
    }
}