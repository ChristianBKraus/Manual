package jupiterpa.manual.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jupiterpa.manual.domain.service.Task;
import jupiterpa.manual.domain.service.TaskExecutor;

@Service 
@Profile("mock")
public class TaskExecutorMock implements ExecutorMocking, TaskExecutor {
    Task task;
    int delay;
	
	@Override
	public void schedule(Task task, int delay) {
		this.task = task;
		this.delay = delay;
	}

	@Override
	public Task getTask() {
		return task;
	}

	@Override
	public int getDelay() {
		return delay;
	}

}
