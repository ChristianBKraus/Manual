package jupiterpa.manual.mock;

import jupiterpa.manual.domain.service.Task;

public interface ExecutorMocking {
	public Task getTask();
	public int getDelay();
}
