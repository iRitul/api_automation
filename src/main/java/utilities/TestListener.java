package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import static org.apache.commons.lang3.StringUtils.repeat;

public class TestListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    private void logMessage(final String message) {
        LOGGER.info("\n");
        LOGGER.info(repeat("=", 75));
        LOGGER.info(message);
        LOGGER.info(repeat("=", 75));
        LOGGER.info("\n");
    }

    @Override
    public void onTestStart(final ITestResult result) {
        logMessage("Test Execution Started...." + result.getName());
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        logMessage("Test Passed Successfully." + result.getName());
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        logMessage("Test Failed!!!!" + result.getName());
    }

    @Override
    public void onFinish(final ITestContext context) {
        logMessage("Test Execution Completed Successfully for all tests!!" + context.getSuite().getAllMethods());
    }

}