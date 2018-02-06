package jupiterpa.manual.domain.service;

public interface TaskExecutor {
	public void schedule(Task task, int delay);
}
