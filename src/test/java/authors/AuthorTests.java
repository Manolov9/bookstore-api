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

    @Test
    public void updateAuthor_shouldReturn200_andUpdatedData() {
        String json = """
            {
              "id": 1,
              "idBook": 1,
              "firstName": "UpdatedFirstName",
              "lastName": "UpdatedLastName"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/Authors/1")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("UpdatedFirstName"))
                .body("lastName", equalTo("UpdatedLastName"));
    }

    @Test
    public void deleteAuthor_shouldReturn200or204() {
        given()
                .when()
                .delete("/api/v1/Authors/9999")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    public void deleteAuthor_shouldDeleteCreatedAuthor() {
        String newAuthorJson = "{"
                + "\"id\": 54321,"
                + "\"idBook\": 1,"
                + "\"firstName\": \"Temp\","
                + "\"lastName\": \"Author\""
                + "}";

        // Created a specific
        given()
                .contentType(ContentType.JSON)
                .body(newAuthorJson)
                .when()
                .post("/api/v1/Authors")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        // Deletion
        given()
                .when()
                .delete("/api/v1/Authors/54321")
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // Deletion and validation of exactly what the creation was
        given()
                .when()
                .get("/api/v1/Authors/54321")
                .then()
                .statusCode(404);
    }

}
