package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestConfig {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://fakerestapi.azurewebsites.net";
    }
}
