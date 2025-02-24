
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.util.SystemPropertiesPropertySource;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Random;

import static io.restassured.RestAssured.given;


public class AuthLogin {
    private static String token;

    // Need to call this before to get accessToken
    @Test(priority = 0)
        //Run|Debug
    void Login() {

        String requestBody = "{\n" +
                "    \"appVersion\": \"2.7.1\",\n" +
                "    \"channel\": \"qmobile\",\n" +
                "    \"imei\": \"0d699d08c023098f\",\n" +
                "    \"msisdn\": \"0712237561\",\n" +
                "    \"pin\": \"2400\"\n" +
                "}";
        Response response = given().
                header("Content-Type", "application/json").
               // header("Authorization", "Bearer 3beafd073ab02721cda6d5a9868524e7")
                body(requestBody).
                when().
                post("http://10.0.0.26:8085/api/v1/auth/login");

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : " + response.getBody().asString());
        System.out.println("Body : " + response.getBody());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("header : " + response.getHeader("content-type"));

        String responseBody = response.getBody().asString();
        String contentType = response.getHeader("content-type");
        if (contentType != null && contentType.contains("json")) {
            int statusCode = response.getStatusCode();
            ObjectMapper mapper = new ObjectMapper();

            try {
                // Parse response as JSON.....
                JsonNode jsonNode = mapper.readTree(responseBody);

                if (jsonNode.has("token")) {
                    JsonNode tokensNode = jsonNode.get("token");

                    // QMAPI has different structure to retrieve token//
                    if (tokensNode.has("JWT")) {
                        String accessToken = tokensNode.get("JWT").asText();

                        System.out.println("Access Token: " + accessToken);

                        token = accessToken;
                    } else {
                        System.out.println("accessToken or refreshToken is missing in the tokens object.");
                    }
                } else {
                    System.out.println("Tokens field is missing in the response.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to process response body", e);
            }

            // Assert that the status code is 200..
            Assert.assertEquals(statusCode, 200);
        } else {
            System.out.println("Unexpected response content type: " + contentType);
        }
    }
    @Test(priority = 1)
        //Run|Debug
    void InvalidPassword() {
        String requestBody = "{\n" +
                "    \"appVersion\": \"2.7.1\",\n" +
                "    \"channel\": \"qmobile\",\n" +
                "    \"imei\": \"958bf15231651ae3\",\n" +
                "    \"msisdn\": \"0626861104\",\n" +
                "    \"pin\": \"24009\"\n" +
                "}";
        Response response = given().
                header("Content-Type", "application/json").
                header("Authorization", "Bearer "+token).
                body(requestBody).
                when().
                post("http://10.0.0.26:8085/api/v1/auth/login");

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : " + response.getBody().asString());

        System.out.println("Body : " + response.getBody());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("header : " + response.getHeader("content-type"));

        String responseBody = response.getBody().asString();
        String contentType = response.getHeader("content-type");
        if
        (contentType != null && contentType.contains("json")) {
            int statuscode = response.statusCode();
            Assert.assertEquals(statuscode, 400);

            Assert.assertTrue(responseBody.contains("errorCode"));
            System.out.println("errorCode:" + responseBody);
        } else {
            System.out.println("Unexpected response code : " + response.statusCode());
        }
    }

    @Test(priority = 2)
        //Run|Debug
    void PINRequired() {
        String requestBody = "{\n" +
                "    \"appVersion\": \"2.7.1\",\n" +
                "    \"channel\": \"qmobile\",\n" +
                "    \"imei\": \"958bf15231651ae3\",\n" +
                "    \"msisdn\": \"0626861104\",\n" +
                "    \"pin\": \"\"\n" +
                "}";
        Response response = given().
                header("Content-Type", "application/json").
                header("Authorization", "Bearer "+token).
                        body(requestBody).
                when().
                post("http://10.0.0.26:8085/api/v1/auth/login");

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : " + response.getBody().asString());
        System.out.println("Body : " + response.getBody());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("header : " + response.getHeader("content-type"));

        String responseBody = response.getBody().asString();
        String contentType = response.getHeader("content-type");
        if
        (contentType != null && contentType.contains("json")) {
            int statuscode = response.statusCode();
            Assert.assertEquals(statuscode, 400);

            Assert.assertTrue(responseBody.contains("errorCode"));
            System.out.println("errorCode:" + responseBody);
        } else {
            System.out.println("Unexpected response code : " + response.statusCode());
        }
    }

    @Test(priority = 3)
        //Run|Debug
    void UserCredentialsRequired() {
        String requestBody = "{\n" +
                "    \"appVersion\": \"2.7.1\",\n" +
                "    \"channel\": \"qmobile\",\n" +
                "    \"imei\": \"958bf15231651ae3\",\n" +
                "    \"msisdn\": \"\",\n" +
                "    \"pin\": \"\"\n" +
                "}";
        Response response = given().
                header("Content-Type", "application/json").
                header("Authorization", "Bearer "+token).
                body(requestBody).
                when().
                post("http://10.0.0.26:8085/api/v1/auth/login");

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : " + response.getBody().asString());
        System.out.println("Body : " + response.getBody());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("header : " + response.getHeader("content-type"));

        String responseBody = response.getBody().asString();
        String contentType = response.getHeader("content-type");
        if
        (contentType != null && contentType.contains("json")) {
            int statuscode = response.statusCode();
            Assert.assertEquals(statuscode, 400);

            Assert.assertTrue(responseBody.contains("errorCode"));
            System.out.println("errorCode:" + responseBody);
        } else {
            System.out.println("Unexpected response code : " + response.statusCode());
        }
    }

    @Test(priority = 4)
        //Run|Debug
    void MSISDNRequired() {
        String requestBody = "{\n" +
                "    \"appVersion\": \"2.7.1\",\n" +
                "    \"channel\": \"qmobile\",\n" +
                "    \"imei\": \"958bf15231651ae3\",\n" +
                "    \"msisdn\": \"\",\n" +
                "    \"pin\": \"2400\"\n" +
                "}";
        Response response = given().
                header("Content-Type", "application/json").
                header("Authorization", "Bearer "+token).
                body(requestBody).
                when().
                post("http://10.0.0.26:8085/api/v1/auth/login");

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : " + response.getBody().asString());
        System.out.println("Body : " + response.getBody());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("header : " + response.getHeader("content-type"));

        String responseBody = response.getBody().asString();
        String contentType = response.getHeader("content-type");
        if
        (contentType != null && contentType.contains("json")) {
            int statuscode = response.statusCode();
            Assert.assertEquals(statuscode, 400);

            Assert.assertTrue(responseBody.contains("errorCode"));
            System.out.println("errorCode:" + responseBody);
        } else {
            System.out.println("Unexpected response code : " + response.statusCode());
        }
    }
}