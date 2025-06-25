package books;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAndUpdateBooksTest extends TestConfig {

    private int createdBookId;

    @BeforeEach
    public void createBook() {
        String json = "{"
                + "\"id\": 54321,"
                + "\"title\": \"Temp Book\","
                + "\"description\": \"Temporary Description\","
                + "\"pageCount\": 100,"
                + "\"excerpt\": \"Temporary excerpt\","
                + "\"publishDate\": \"2025-01-01T00:00:00Z\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/Books")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        createdBookId = 54321;
    }

    @AfterEach
    public void deleteBook() {
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
                + "\"title\": \"Updated Book Title\","
                + "\"description\": \"Updated Description\","
                + "\"pageCount\": 150,"
                + "\"excerpt\": \"Updated excerpt text\","
                + "\"publishDate\": \"2025-12-31T00:00:00Z\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Book Title"))
                .body("description", equalTo("Updated Description"));
    }

    @Test
    @Order(2)
    public void deleteBook_shouldReturn200or204() {
        given()
                .when()
                .delete("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(anyOf(is(200), is(204)));

        given()
                .when()
                .get("/api/v1/Books/" + createdBookId)
                .then()
                .statusCode(404);
    }

    // Тук оставяме останалите тестове без @Order, тъй като не зависят от създадената книга

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
