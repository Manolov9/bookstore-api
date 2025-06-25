package authors;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthorTests extends TestConfig {

    @Test
    public void getAllAuthors_shouldReturn200() {
        given()
        .when().get("/api/v1/Authors")
        .then().statusCode(200)
               .contentType(ContentType.JSON)
               .body("size()", greaterThan(0));
    }

    @Test
    public void getAuthorById_invalid_shouldReturn404() {
        given()
        .when().get("/api/v1/Authors/9999")
        .then().statusCode(404);
    }

    @Test
    public void createAuthor_shouldReturn201() {
        String json = "{\"id\":2001,\"idBook\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        given()
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post("/api/v1/Authors")
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .body("firstName", equalTo("John"));
    }
}
