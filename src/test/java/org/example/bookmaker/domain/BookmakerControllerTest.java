package org.example.bookmaker.domain;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.restassured.RestAssured;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = BookmakerController.class) // test slice annotations
@Testcontainers
class BookmakerControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    @Autowired
    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Sql("/test_data.sql")
    void shouldGetBookmarksByPage() {
        given().contentType("application/json")
                .when()
                .get("/api/v1/bookmarks?pageNo=1&pageSize=10")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(10))
                .body("totalElements", equalTo(15))
                .body("pageNumber", equalTo(0))
                .body("totalPage", equalTo(2))
                .body("isFirst", equalTo(true))
                .body("isLast", equalTo(false))
                .body("hasNext", equalTo(true))
                .body("hasPrevious", equalTo(false));
    }

    @Test
    void shouldCreateBookmarkSuccessfully() {
        given().contentType(ContentType.JSON).body(
                        """
                                {
                                "title": "Bealdung blog",
                                "url": "illimitablemen.com"
                                }
                                """
                )
                .when()
                .post("/api/v1/updateBookmarks")
                .then()
                .statusCode(201) //created
                .header("Location", matchesRegex(".*/api/v1/updateBookmarks/[0-9]+$"))
                .body("id", notNullValue())
                .body("title", equalTo("Bealdung blog"))
                .body("url", equalTo("https://illimitablemen.org"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

    }

    @Test
    void shouldUpdateBookmarkSuccessfully() {
        BookmarkRequest request = new BookmarkRequest("x platform","https://www.x.com");
        BookmarkDTO bookmarks = bookmarkService.create(request);
        given().contentType(ContentType.JSON)
                .body(
                        """
                                {
                                "title": "x platform",
                                "url": "https://www.x.com"
                                }
                                """
                )
                .when()
                .put("/api/v1/updateBookmarks/{id}", bookmarks.id())
                .then()
                .statusCode(200); // ok
    }


    @Test
    void shouldGetBookmarkByIdSuccessfully(){
        BookmarkRequest request = new BookmarkRequest("x platform","https://www.x.com");
        BookmarkDTO bookmarks = bookmarkService.create(request);
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/bookmarks/{id}", bookmarks.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(bookmarks.id()))
                .body("title", equalTo("x platform"))
                .body("url", equalTo("https://www.x.com"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    void shouldDeleteBookmarkByIdSuccessfully(){
        BookmarkRequest request = new BookmarkRequest("x platform","https://www.x.com");
        BookmarkDTO bookmarks = bookmarkService.create(request);
        given().contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/bookmarks/{id}", bookmarks.id())
                .then()
                .statusCode(200);

        //verify
        Optional<BookmarkDTO> bookmark = bookmarkService.findById(bookmarks.id());
//        assertThat(bookmark).isEmpty();
    }

    @Test
    void shouldGet404whenBookmarkNotExist(){
        Long nonExistingId = 9999L;
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/bookmarks/{id}", nonExistingId)
                .then()
                .statusCode(404);
    }
}