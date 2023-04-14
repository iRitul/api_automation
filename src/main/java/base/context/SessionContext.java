/* (C) Games24x7 */
package base.context;

import java.util.HashMap;

public class SessionContext {
	private static final HashMap<String, TestExecutionContext> allTestsExecutionContext;

	static {
		new SessionContext();
		allTestsExecutionContext = new HashMap<>();
	}

	static synchronized void addContext(long threadId, TestExecutionContext testExecutionContext) {
		allTestsExecutionContext.put(String.valueOf(threadId), testExecutionContext);
	}

	public static synchronized TestExecutionContext getTestExecutionContext(long threadId) {
		return allTestsExecutionContext.get(String.valueOf(threadId));
	}

	public static synchronized void remove(long threadId) {
		allTestsExecutionContext.remove(String.valueOf(threadId));
	}
}
