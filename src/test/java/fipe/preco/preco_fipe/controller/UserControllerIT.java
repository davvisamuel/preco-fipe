package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.config.RestAssuredConfiguration;
import fipe.preco.preco_fipe.config.TestcontainersConfiguration;
import fipe.preco.preco_fipe.repository.UserRepository;
import fipe.preco.preco_fipe.utils.FileUtils;
import fipe.preco.preco_fipe.utils.UserUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfiguration.class)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerIT {
    @Autowired
    private RequestSpecification requestSpecification;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("GET v1/user returns all users when successful")
    @Order(1)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAllUsers_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/user/get-user-response-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("v1/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);


        JsonAssertions.assertThatJson(response)
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                });
    }

    @Test
    @DisplayName("GET v1/user returns an empty list when successful")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNoUsers() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/user/get-user-empty-list-response-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("v1/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/user/{id} returns a user when id is found")
    @Order(3)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsUser_WhenIdIsFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/user/get-user-by-id-response-200.json");

        var id = repository.findAll().getFirst().getId();

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("v1/user/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);

        JsonAssertions.assertThatJson(response)
                .inPath("id")
                .asNumber()
                .isPositive();
    }

    @Test
    @DisplayName("GET v1/user/{id} ThrowsNotFoundException when id is not found")
    @Order(4)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/user/get-user-by-id-response-404.json");

        var id = 99L;

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("v1/user/{id}", id)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("POST v1/user create a user when valid fields")
    @Order(5)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesUser_WhenValidFields() throws IOException {
        var request = fileUtils.readResourceFile("/user/post-user-request-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("v1/user")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().body().asString();

        var savedUser = repository.findAll().getFirst();

        var expectedResponse = fileUtils.readResourceFile("/user/post-user-response-201.json")
                .replace("1", savedUser.getId().toString());

        JsonAssertions.assertThatJson(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("PUT v1/user updated a user when id is found")
    @Order(6)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_UpdatesUser_WhenIdIsFound() throws IOException {
        var savedUser = repository.findAll().getFirst();

        var userToUpdate = userUtils.newUserToUpdate()
                .withId(savedUser.getId());

        var request = fileUtils.readResourceFile("/user/put-user-request-200.json")
                .replace("1", savedUser.getId().toString());

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("v1/user")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var updatedUser = repository.findById(savedUser.getId());

        Assertions.assertThat(updatedUser).isPresent();
        Assertions.assertThat(updatedUser.get()).isEqualTo(userToUpdate);
    }
}