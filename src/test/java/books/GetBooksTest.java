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

    @Test
    public void updateBook_shouldReturn200_andUpdatedData() {
        String json = """
            {
              "id": 1,
              "title": "Updated Book Title",
              "description": "Updated Description",
              "pageCount": 150,
              "excerpt": "Updated excerpt text",
              "publishDate": "2025-12-31T00:00:00Z"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/Books/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Book Title"))
                .body("description", equalTo("Updated Description"));
    }

    @Test
    public void deleteBook_shouldReturn200or204() {
        given()
                .when()
                .delete("/api/v1/Books/9999")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    public void deleteBook_shouldDeleteCreatedBook() {
        // A new book creation
        String newBookJson = "{"
                + "\"id\": 12345,"
                + "\"title\": \"Book to Delete\","
                + "\"description\": \"Temporary\","
                + "\"pageCount\": 100,"
                + "\"excerpt\": \"Excerpt\","
                + "\"publishDate\": \"2025-01-01T00:00:00Z\""
                + "}";

        // Setup
        given()
                .contentType(ContentType.JSON)
                .body(newBookJson)
                .when()
                .post("/api/v1/Books")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        // Deletion a book by ID
        given()
                .when()
                .delete("/api/v1/Books/12345")
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // Validation by ID for already deleted previously deleted specific ID
        given()
                .when()
                .get("/api/v1/Books/12345")
                .then()
                .statusCode(404);
    }

}
