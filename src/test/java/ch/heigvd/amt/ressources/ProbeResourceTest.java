package ch.heigvd.amt.ressources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ProbeResourceTest {

    @Test
    void index() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body(containsString("<title>Homepage</title>"))
             .body(containsString("Uptime"));
    }

    @Test
    void probes() {
        given()
                .when().get("/probes")
                .then()
                .statusCode(200)
                .body(containsString("<title>Uptime probes</title>"))
                .body(containsString("<h1>Probes</h1>"));
    }

    @Test
    void status() {
        given()
                .when().get("/status?url=https://httpstat.us/200")
                .then()
                .statusCode(200)
                .body(containsString("<title>Uptime status</title>"))
                .body(containsString("<h1>Status</h1>"));
    }

    @Test
    void statusContent() {
        given()
                .when().get("/status?url=https://httpstat.us/200")
                .then()
                .statusCode(200)
                .body(containsString("<h1>Status</h1>"));
    }

    @Test
    void register() {
        given()
                .when().get("/register")
                .then()
                .statusCode(200)
                .body(containsString("<title>Register a new probe</title>"))
                .body(containsString("<h1>Register a new probe</h1>"));
    }

}