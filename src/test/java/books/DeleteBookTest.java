package books;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteBookTest extends TestConfig {

    private int createdBookId;

    @BeforeEach
    public void createBook() {
        String json = "{"
                + "\"id\": 123456,"
                + "\"title\": \"Book for Test\","
                + "\"description\": \"Desc\","
                + "\"pageCount\": 100,"
                + "\"excerpt\": \"Excerpt\","
                + "\"publishDate\": \"2025-01-01T00:00:00Z\""
                + "}";

        // Create a book before each test
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/Books")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        createdBookId = 123456;
    }

    @AfterEach
    public void deleteBook() {
        // Delete the book after each test to clean up
        given()
                .when()
                .delete("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
    }

    @Test
    @Order(1)
    public void updateBook_shouldReturn200_andUpdatedData() {
        String json = "{"
                + "\"id\": " + createdBookId + ","
                + "\"title\": \"Updated Title\","
                + "\"description\": \"Updated Desc\","
                + "\"pageCount\": 111,"
                + "\"excerpt\": \"Updated excerpt\","
                + "\"publishDate\": \"2025-12-31T00:00:00Z\""
                + "}";

        // Update the book and verify the response
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .body("description", equalTo("Updated Desc"));
    }

    @Test
    @Order(2)
    public void deleteBook_shouldReturn200or204() {
        // Delete the book
        given()
                .when()
                .delete("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // Verify the book is deleted
        given()
                .when()
                .get("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(404);
    }
}
