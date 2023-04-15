package utilities;

import java.util.Map;
import static utilities.JsonUtil.loadJsonFile;

public class DataUtil {

    private static final String ENVIRONMENT_FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/environment.json";
    private static final String TEST_DATA_FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/test_data.json";
    private static Map<String, Object> environmentData;
    private static Map<String, Object> testData;

    public static void loadEnvironmentData(String environment) {
        environmentData = (Map<String, Object>) loadJsonFile(ENVIRONMENT_FILE_PATH).get(environment);
    }

    public static void loadTestData(String environment) {
        testData = (Map<String, Object>) loadJsonFile(TEST_DATA_FILE_PATH).get(environment);
    }

    public static Object getEnvironmentData(String key) {
        return environmentData.get(key);
    }

    public static Object getTestData(String key) {
        return testData.get(key);
    }
}
