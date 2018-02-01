package jupiterpa.manual;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jupiterpa.manual.client.ClientMocking;
import jupiterpa.manual.domain.client.*;
import jupiterpa.manual.domain.model.*;
import jupiterpa.manual.intf.controller.Controller;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
@ActiveProfiles({"mock","test"})
public class IntegrationTest { 
	final String PATH = Controller.PATH; 

	@Autowired private MockMvc mockMvc;
	@Autowired private LEDStripClient client;
	@Autowired private ActionRepo repo;
	
	@Before
	public void ResetDB() {
		repo.deleteAll();
	}
	
	@Before
	public void initialize() {
        ClientMocking mock = (ClientMocking) client;
        mock.inject(null);
	}
    
    @Test
    public void test() throws Exception {
        ClientMocking mock = (ClientMocking) client;
        mock.inject(new ArrayList<LED>());

//      Post
    	mockMvc.perform( put( PATH +"/trigger/" + Action.HINWEIS_INES) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(Action.HINWEIS_INES))
        .andExpect(jsonPath("$.status").value(1))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(Action.HINWEIS_INES));
    	
    }
}