package com.api.apiservices;

import Base.BaseSetup;
import utilities.JsonUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import java.util.HashMap;
import java.util.Map;
import static utilities.DataUtil.getTestData;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Send_otp extends BaseSetup {
    private static final Logger LOG = Logger.getLogger(Send_otp.class);
    private static final String API_REQUEST_FILE_PATH = "src/test/java/com/api/requests/";

    private static final String SEND_OTP_API_PATH = "/v2/user_api/user.send_otp";

    @Test
    public void send_otp() {
        LOG.info("Send OTP vanilla case api test started.");
        Gson gson = new Gson();
        String header = gson.toJson(JsonUtil.getNodeValueAsMapFromJsonFile("headers", API_REQUEST_FILE_PATH + "user.send_otp.json"));
        String headerWithAuthToken = String.format(header, GetAuthenticationTokenService.getAuthenticationApi());
        Map<String, Object> userData = new Gson().fromJson(headerWithAuthToken, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        Response response = given()
                .when()
                .headers(userData)
                .queryParam("phone_number", getTestData("mobileNumber").toString())
                .queryParam("country_code", getTestData("country_code").toString())
                .queryParam("channel", getTestData("channel").toString())
                .queryParam("for_phone_update", getTestData("for_phone_update").toString())
                .get(SEND_OTP_API_PATH)
                .then()
                .extract().response();
        JsonPath jsp = new JsonPath(response.asString());
        System.out.println("Response of user.send_otp : " + jsp.getString("message"));
    }

    @Test
    public void validateSendOTPSchema() {
        LOG.info("Send OTP api schema validation test started.");
        Gson gson = new Gson();
        String header = gson.toJson(JsonUtil.getNodeValueAsMapFromJsonFile("headers", API_REQUEST_FILE_PATH + "user.send_otp.json"));
        String headerWithAuthToken = String.format(header, GetAuthenticationTokenService.getAuthenticationApi());
        Map<String, Object> userData = new Gson().fromJson(headerWithAuthToken, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        given()
                .when()
                .headers(userData)
                .queryParam("phone_number", getTestData("mobileNumber").toString())
                .queryParam("country_code", getTestData("country_code").toString())
                .queryParam("channel", getTestData("channel").toString())
                .queryParam("for_phone_update", getTestData("for_phone_update").toString())
                .get(SEND_OTP_API_PATH)
                .then().assertThat().body(matchesJsonSchemaInClasspath("user.send_otp.json"));
    }
}
