package com.api.apiservices;

import utilities.JsonUtil;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;
import static utilities.DataUtil.getEnvironmentData;
import static io.restassured.RestAssured.given;

public class GetAuthenticationTokenService {
    private static final String API_REQUEST_FILE_PATH = "src/test/java/com/api/requests/";

    private static final String AUTH_TOKEN_API_PATH = "/v2/user_api/user.get_authentication_token";

    public static String getAuthenticationApi() {
        RestAssured.baseURI = (String) getEnvironmentData("baseUrl");
        Map<String, Object> header = JsonUtil.loadJsonFile(API_REQUEST_FILE_PATH + "user.get_authentication_token.json");
        Response response = given()
                .when().headers(header)
                .get(AUTH_TOKEN_API_PATH)
                .then()
                .extract().response();
        JsonPath jsp = new JsonPath(response.asString());
        return jsp.getString("result.authentication_info.auth_token").replace("]", "").replace("[", "");
    }

}