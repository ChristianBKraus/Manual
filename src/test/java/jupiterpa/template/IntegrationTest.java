package jupiterpa.template;

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

import jupiterpa.template.client.ClientMocking;
import jupiterpa.template.domain.client.*;
import jupiterpa.template.domain.model.*;
import jupiterpa.template.intf.controller.Controller;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
@ActiveProfiles({"mock","test"})
public class IntegrationTest { 
	final String PATH = Controller.PATH; 

	@Autowired private MockMvc mockMvc;
	@Autowired private TemplateClient client;
	@Autowired private TemplateRepo repo;
	
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
        mock.inject(new ArrayList<TemplateEntity>());

        TemplateEntity entity = new TemplateEntity("Testing");

//      Post
    	mockMvc.perform( post(PATH).content(toJson(entity)).contentType(APPLICATION_JSON_UTF8) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.value").value("TestingT"));
    	
//      Get
    	mockMvc.perform( get(PATH) )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.value").value("TestingT"));
    	
    }
    private String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    
}