package Base;

import Utilities.TestListener;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.log4testng.Logger;
import static Utilities.DataUtil.*;
import static org.hamcrest.Matchers.lessThan;

public class BaseSetup extends TestListener {
    private static final Logger LOG = Logger.getLogger(BaseSetup.class);
    public static String environment;

    @BeforeSuite
    @Parameters({"environment"})
    public static void setEnvironmentVariable(String env) {
        environment = env;
        loadEnvironmentData(environment);
        loadTestData(environment);
    }

    @BeforeClass
    @Parameters({"environment"})
    public void setup(String environment) {
        String url = (String) getEnvironmentData("baseUrl");
        LOG.info("Environment :" + environment);
        LOG.info("Base Url :" + url);
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri(url).addHeader("Content-Type", "application/json").addHeader("Accept", "application/json").addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter()).build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectResponseTime(lessThan(20000L)).build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;

    }

}