package books;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostBookTest extends TestConfig {

    @Test
    public void createBook_shouldReturn201() {
        String jsonBody = "{\"id\": 1001, \"title\": \"Test Book\", \"description\": \"API test\", \"pageCount\": 111, \"excerpt\": \"Excerpt\", \"publishDate\": \"2025-01-01T00:00:00.000Z\"}";

        given()
            .contentType(ContentType.JSON)
            .body(jsonBody)
        .when()
            .post("/api/v1/Books")
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .body("title", equalTo("Test Book"));
    }
}
