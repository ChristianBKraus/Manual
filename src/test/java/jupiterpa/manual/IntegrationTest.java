package jupiterpa.manual;

import static org.junit.Assert.assertEquals;
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
@WithMockUser(roles="ADMIN", username="user")
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
    public void createAndGet() throws Exception {
    	String id = Action.HINWEIS_INES;
    	
        ClientMocking mock = (ClientMocking) client;
        mock.inject(new ArrayList<LED>());

//      Create / Change
    	mockMvc.perform( put( PATH +"/trigger/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(1))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[0].status").value(1))
        ;
    	
//      Client
    	ArrayList<LED> leds = (ArrayList<LED>) mock.getState();
    	assertEquals(1,leds.size());
    	
    }
    
    @Test
    public void triggerGetUntriggerGet() throws Exception {
    	String id = Action.HINWEIS_JONATHAN;
    	
        ClientMocking mock = (ClientMocking) client;
        mock.inject(new ArrayList<LED>());

//      Trigger
    	mockMvc.perform( put( PATH +"/trigger/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(1))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[0].status").value(1))
        ;

//      UnTrigger
    	mockMvc.perform( put( PATH +"/untrigger/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(0))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[0].status").value(0))
        ;
    }
    
    @Test
    public void triggerTriggerReset() throws Exception {
    	String id = Action.TASCHENGELD_INES;
    	
        ClientMocking mock = (ClientMocking) client;
        mock.inject(new ArrayList<LED>());

//      Trigger
    	mockMvc.perform( put( PATH +"/trigger/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(1))
        ;
    	
//      Trigger
    	mockMvc.perform( put( PATH +"/trigger/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(2))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[0].status").value(2))
        ;

//      Reset
    	mockMvc.perform( post( PATH +"/reset/" + id) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(0))
        ;
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[0].status").value(0))
        ;
    }
    @Test
    @WithMockUser(roles = "Nothing")
    public void securityNone() throws Exception {
    	String id = Action.VERBOT_INES;
    	
//      GET
    	mockMvc.perform( get( PATH ) )
        .andExpect(status().isForbidden());

//      PUT /trigger
    	mockMvc.perform( put( PATH + "/trigger/" + id) )
        .andExpect(status().isForbidden());

//      PUT /untrigger
    	mockMvc.perform( put( PATH + "/untrigger/" + id) )
        .andExpect(status().isForbidden());

//      POST
    	mockMvc.perform( post( PATH + "/reset/" + id) )
        .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    public void securityUser() throws Exception {
    	String id = Action.VERBOT_INES;
    	
//      GET
    	mockMvc.perform( get( PATH ) )
        .andExpect(status().isOk());

//      PUT /trigger
    	mockMvc.perform( put( PATH + "/trigger/" + id) )
        .andExpect(status().isOk());

//      PUT /untrigger
    	mockMvc.perform( put( PATH + "/untrigger/" + id) )
        .andExpect(status().isOk());

//      POST
    	mockMvc.perform( post( PATH + "/reset/" + id) )
        .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void securityAdmin() throws Exception {
    	String id = Action.VERBOT_INES;
    	
//      GET
    	mockMvc.perform( get( PATH ) )
        .andExpect(status().isOk());

//      PUT /trigger
    	mockMvc.perform( put( PATH + "/trigger/" + id) )
        .andExpect(status().isOk());

//      PUT /untrigger
    	mockMvc.perform( put( PATH + "/untrigger/" + id) )
        .andExpect(status().isOk());

//      POST
    	mockMvc.perform( post( PATH + "/reset/" + id) )
        .andExpect(status().isOk());
    }
}