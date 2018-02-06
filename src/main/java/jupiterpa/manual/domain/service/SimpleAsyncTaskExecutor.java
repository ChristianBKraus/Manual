package jupiterpa.manual.domain.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!mock")
public class SimpleAsyncTaskExecutor implements TaskExecutor {

	@Override
	public void schedule(Task task, int delay) {
		org.springframework.core.task.SimpleAsyncTaskExecutor scheduler = new org.springframework.core.task.SimpleAsyncTaskExecutor();
		scheduler.execute(task,  delay );
	}

}
