package authors;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorTests extends TestConfig {

    private int createdAuthorId;

    @BeforeEach
    public void createAuthor() {
        String json = "{"
                + "\"id\": 54321,"
                + "\"idBook\": 1,"
                + "\"firstName\": \"Temp\","
                + "\"lastName\": \"Author\""
                + "}";

        // Create author before each test
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/Authors")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        createdAuthorId = 54321;
    }

    @AfterEach
    public void deleteAuthor() {
        // Clean up by deleting the created author after each test
        given()
                .when()
                .delete("/api/v1/Authors/" + createdAuthorId)
                .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
    }

    @Test
    @Order(1)
    public void updateAuthor_shouldReturn200_andUpdatedData() {
        String json = "{"
                + "\"id\": " + createdAuthorId + ","
                + "\"idBook\": 1,"
                + "\"firstName\": \"UpdatedFirstName\","
                + "\"lastName\": \"UpdatedLastName\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/Authors/" + createdAuthorId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("UpdatedFirstName"))
                .body("lastName", equalTo("UpdatedLastName"));
    }

    @Test
    @Order(2)
    public void deleteAuthor_shouldReturn200or204() {
        given()
                .when()
                .delete("/api/v1/Authors/" + createdAuthorId)
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // Verify deletion
        given()
                .when()
                .get("/api/v1/Authors/" + createdAuthorId)
                .then()
                .statusCode(404);
    }

    // Останалите тестове, които не се нуждаят от create/delete setup, остават без промяна

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
