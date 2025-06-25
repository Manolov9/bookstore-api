package books;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBooksTest extends TestConfig {

    @Test
    public void getAllBooks_shouldReturn200() {
        given()
        .when().get("/api/v1/Books")
        .then().statusCode(200)
               .contentType(ContentType.JSON)
               .body("size()", greaterThan(0));
    }

    @Test
    public void getBookById_validId_shouldReturnBook() {
        given()
        .when().get("/api/v1/Books/1")
        .then().statusCode(200)
               .body("id", equalTo(1));
    }

    @Test
    public void getBookById_invalidId_shouldReturn404() {
        given()
        .when().get("/api/v1/Books/99999")
        .then().statusCode(404);
    }
}
