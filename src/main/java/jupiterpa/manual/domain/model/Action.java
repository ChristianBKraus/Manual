package jupiterpa.manual.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class Action {
	
	@Id
	String id;
	int status;
	boolean action;
	
	public final static String TASCHENGELD_JONATHAN = "TaschengeldJonathan";
	public final static String TASCHENGELD_INES = "TaschengeldInes";
	public final static String VERBOT_JONATHAN = "VerbotJonathan";
	public final static String VERBOT_INES = "VerbotInes";
	public final static String HINWEIS_JONATHAN = "HinweisJonathan";
	public final static String HINWEIS_INES = "HinweisInes";

	public Action() {}
	public Action(String id, boolean action) {
		this.id = id;
		this.status = 0;
		this.action = action;
	}
	public void reset() {
		status = 0;
	}
	public int trigger(int inc) {
		status += inc;
		if (status < 0)
			status = 0;
		return status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return "Action " + id + ": " + status + "(" + action + ")"; 
	}
}
