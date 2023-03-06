package Exceptions;

public class TestExecutionException extends RuntimeException {
    public TestExecutionException(String failureMessage) {
        super(failureMessage);
    }
}
