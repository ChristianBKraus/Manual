package jupiterpa.manual.intf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.util.*;
import jupiterpa.manual.domain.model.*;
import jupiterpa.manual.domain.service.*;


@RequestMapping(path = Controller.PATH)
@RestController
@Api(value="manual", description="Manual Action Controller")
public class Controller {
    public static final String PATH ="/manual";
    
    @Autowired ActionRepo repo;
    @Autowired ActionService service;
    
    @GetMapping("")
    @ApiOperation(value = "GET all Actions", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Actions")
    })
    public List<Action> get() {  
    	return repo.findAll();
    }
    
    @PutMapping("/trigger/{name}")
    @ApiOperation(value = "Trigger status change for action")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Action triggered")
    })
    public Action trigger(@PathVariable String name) {
    	return service.action(name,1);
    }

    @PutMapping("/untrigger/{name}")
    @ApiOperation(value = "Trigger status change for action")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Action untriggered")
    })
    public Action untrigger(@PathVariable String name) {
    	return service.action(name,-1);
    }

    @PostMapping("/reset/{name}")
    @ApiOperation(value = "Reset status for action")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Status resetted")
    })
    public Action reset(@PathVariable String name) {
    	return service.action(name,0);
    }

}
