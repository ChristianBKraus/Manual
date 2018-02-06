package jupiterpa.manual.domain.service;

import org.springframework.beans.factory.annotation.Autowired;

public class Task implements Runnable {
	String action;
	int    value;
	@Autowired ActionService service;
	public Task(String action, int value) {
		this.action = action;
		this.value = value;
	}
	@Override
	public void run() {
		service.action(action, value);
	}
	public String getAction() {
		return action;
	}
	public int getValue() {
		return value;
	}
}