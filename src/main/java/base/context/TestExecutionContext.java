package base.context;

import java.util.HashMap;

public class TestExecutionContext {
	private final HashMap<String, Object> testExecutionState;

	public TestExecutionContext() {
		long threadId = Thread.currentThread().getId();
		SessionContext.addContext(threadId, this);
		this.testExecutionState = new HashMap<String, Object>();
	}

	public void addTestState(String key, Object details) {
		testExecutionState.put(key, details);
	}

	public Object getTestState(String key) {
		return testExecutionState.get(key);
	}

	public String getTestStateAsString(String key) {
		return (String) testExecutionState.get(key);
	}
}
