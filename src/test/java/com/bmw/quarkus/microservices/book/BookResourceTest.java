package com.bmw.quarkus.microservices.book;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
class BookResourceTest {

    @Test
    public void shouldCreateBook() {
        given()
                .formParam("title", "Quarkus for Dummies")
                .formParam("author", "John Doe")
                .formParam("year", 2021)
                .formParam("genre", "Programming")
                .when().post("/api/books")
                .then()
                .statusCode(201)
                .body("title", is("Quarkus for Dummies"))
                .body("author", is("John Doe"))
                .body("year_of_publication", is(2021))
                .body("isbn_13", startsWith("13-:"))
                .body("genre", is("Programming"));
    }
}
