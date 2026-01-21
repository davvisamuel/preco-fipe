package fipe.preco.preco_fipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fipe.preco.preco_fipe.config.RestAssuredConfiguration;
import fipe.preco.preco_fipe.config.TestcontainersConfiguration;
import fipe.preco.preco_fipe.dto.request.UserEmailPutRequest;
import fipe.preco.preco_fipe.repository.UserRepository;
import fipe.preco.preco_fipe.security.TokenService;
import fipe.preco.preco_fipe.utils.AuthUtils;
import fipe.preco.preco_fipe.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfiguration.class)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerIT {
    @Autowired
    private RequestSpecification requestSpecification;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("GET v1/user returns user page when successful")
    @Order(1)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllPaginated_ReturnsUserPage_WhenSuccessful() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var expectedResponse = fileUtils.readResourceFile("/user/get-user-response-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .when()
                .get("/v1/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("content[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/user/{id} returns a user when id is found")
    @Order(2)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsUser_WhenIdIsFound() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var expectedResponse = fileUtils.readResourceFile("/user/get-user-by-id-response-200.json");

        var id = repository.findAll().getFirst().getId();

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .when()
                .get("/v1/user/{id}", id)
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
    @Order(3)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var expectedResponse = fileUtils.readResourceFile("/user/get-user-by-id-response-404.json");

        var id = 99L;

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .when()
                .get("/v1/user/{id}", id)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("POST v1/user create a user when valid fields")
    @Order(4)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesUser_WhenValidFields() throws IOException {
        var request = fileUtils.readResourceFile("/user/post-user-request-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/user/register")
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
    @Order(5)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_UpdatesUser_WhenIdIsFound() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-user-request-200.json");

        var id = tokenService.validateLogin(userToken);

        var savedUser = repository.findById(Integer.valueOf(id));

        var request = fileUtils.readResourceFile("/user/put-user-request-200.json");

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .body(request)
                .when()
                .put("/v1/user")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var updatedUser = repository.findById(Integer.valueOf(id));

        Assertions.assertThat(updatedUser).isPresent();

        Assertions.assertThat(updatedUser).isNotEqualTo(savedUser);
    }

    @Test
    @DisplayName("PUT v1/user Throws BadRequest 400 when email already exists")
    @Order(6)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ThrowEmailAlreadyExists_WhenEmailAlreadyExists() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-user-request-200.json");

        var request = fileUtils.readResourceFile("/user/put-user-request-400.json");

        var expectedResponse = fileUtils.readResourceFile("/user/put-user-response-400.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .body(request)
                .when()
                .put("/v1/user")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/user removes a user when id is found")
    @Order(7)
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_RemovesUser_WhenIdIsFound() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-user-request-200.json");

        var id = tokenService.validateLogin(userToken);

        var userToDelete = repository.findById(Integer.valueOf(id));

        RestAssured.given()
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .when()
                .delete("/v1/user")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var allUsers = repository.findAll();

        Assertions.assertThat(userToDelete).isNotEmpty();

        Assertions.assertThat(allUsers)
                .isNotNull()
                .doesNotContain(userToDelete.get());
    }

    @ParameterizedTest
    @MethodSource("providedPostArguments")
    @DisplayName("POST v1/user throws bad request 400 when invalid fields")
    @Order(8)
    void save_ReturnsBadRequest_WhenInvalidFields(String requestPath, String expectedResponsePath) throws IOException {
        var request = fileUtils.readResourceFile(requestPath);

        var expectedResponse = fileUtils.readResourceFile(expectedResponsePath);

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/user/register")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/user/email returns 204 no content when successful")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateEmail_UpdatesEmail_WhenSuccessful() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var request = fileUtils.readResourceFile("/user/put-user-email-request-200.json");

        var userEmailPutRequest = objectMapper.readValue(request, UserEmailPutRequest.class);

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .body(request)
                .when()
                .put("/v1/user/email")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var optionalUser = repository.findByEmail(userEmailPutRequest.getNewEmail());

        Assertions.assertThat(optionalUser)
                .isNotEmpty();

        Assertions.assertThat(optionalUser.get())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(10)
    @DisplayName("PUT v1/user/email returns 403 FORBIDDEN when password is incorrect")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateEmail_ThrowsInvalidPasswordException_WhenIncorrectPassword() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var request = fileUtils.readResourceFile("/user/put-user-email-request-403.json");

        var expectedResponse = fileUtils.readResourceFile("/user/put-user-email-response-403.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .body(request)
                .when()
                .put("/v1/user/email")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .log().all()
                .extract().body().asString();


        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(11)
    @DisplayName("PUT v1/user/email returns 400 BAD REQUEST when email already exists")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateEmail_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() throws IOException {
        var userToken = authUtils.login("/auth/post-auth-admin-request-200.json");

        var request = fileUtils.readResourceFile("/user/put-user-email-request-400.json");

        var expectedResponse = fileUtils.readResourceFile("/user/put-user-email-response-400.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + userToken)
                .body(request)
                .when()
                .put("/v1/user/email")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("providedPutEmailArguments")
    @Order(12)
    @DisplayName("updateEmail returns 400 bad request when invalid fields")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateEmail_ThrowsBadRequest_WhenInvalidFields(String requestPath, String expectedResponseBodyPath) throws IOException {
        var requestBody = fileUtils.readResourceFile(requestPath);
        var expectedResponseBody = fileUtils.readResourceFile(expectedResponseBodyPath);

        var token = authUtils.login("/auth/post-auth-admin-request-200.json");

        var responseBody = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .auth().oauth2("Bearer " + token)
                .when()
                .put("/v1/user/email")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(responseBody)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponseBody);
    }

    @ParameterizedTest
    @MethodSource("providedPutPasswordArguments")
    @Order(13)
    @DisplayName("updatePassword returns 400 bad request when invalid fields")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updatePassword_ThrowsBadRequest_WhenInvalidFields(String requestPath, String expectedResponseBodyPath) throws IOException {
        var requestBody = fileUtils.readResourceFile(requestPath);
        var expectedResponseBody = fileUtils.readResourceFile(expectedResponseBodyPath);

        var token = authUtils.login("/auth/post-auth-admin-request-200.json");

        var responseBody = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .auth().oauth2("Bearer " + token)
                .when()
                .put("/v1/user/password")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(responseBody)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponseBody);
    }


    private static Stream<Arguments> providedPostArguments() {
        return Stream.of(
                Arguments.of("/user/post-user-blank-fields-request-400.json", "/user/post-user-blank-fields-response-400.json"),
                Arguments.of("/user/post-user-empty-fields-request-400.json", "/user/post-user-empty-fields-response-400.json"),
                Arguments.of("/user/post-user-null-fields-request-400.json", "/user/post-user-null-fields-response-400.json")

        );
    }

    private static Stream<Arguments> providedPutEmailArguments() {
        return Stream.of(
                Arguments.of("/user/put-user-email-blank-fields-request-400.json", "/user/put-user-email-blank-fields-response-400.json"),
                Arguments.of("/user/put-user-email-empty-fields-request-400.json", "/user/put-user-email-empty-fields-response-400.json"),
                Arguments.of("/user/put-user-email-null-fields-request-400.json", "/user/put-user-email-null-fields-response-400.json")
        );
    }

    private static Stream<Arguments> providedPutPasswordArguments() {
        return Stream.of(
                Arguments.of("/user/put-user-password-blank-fields-request-400.json", "/user/put-user-password-blank-fields-response-400.json"),
                Arguments.of("/user/put-user-password-empty-fields-request-400.json", "/user/put-user-password-empty-fields-response-400.json"),
                Arguments.of("/user/put-user-password-null-fields-request-400.json", "/user/put-user-password-null-fields-response-400.json")
        );
    }
}