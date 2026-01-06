package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.config.RestAssuredConfiguration;
import fipe.preco.preco_fipe.config.TestcontainersConfiguration;
import fipe.preco.preco_fipe.domain.Favorite;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.repository.FavoriteRepository;
import fipe.preco.preco_fipe.repository.UserRepository;
import fipe.preco.preco_fipe.utils.AuthUtils;
import fipe.preco.preco_fipe.utils.FavoriteUtils;
import fipe.preco.preco_fipe.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FavoriteControllerIT {

    @Autowired
    private RequestSpecification requestSpecification;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @Order(1)
    @DisplayName("save creates favorite when valid fields")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_favorites.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesFavorite_WhenValidFields() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var request = fileUtils.readResourceFile("/favorite/favorite-post-request-200.json");

        var response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .body(request)
                .when()
                .post("/v1/favorite")
                .then()
                .statusCode(HttpStatus.CREATED)
                .log().all()
                .extract().body().asString();

        var favorites = favoriteRepository.findAll();

        Assertions.assertThat(favorites).isNotNull().hasSize(1);

        JsonAssertions.assertThatJson(response)
                .inPath("id")
                .isNumber()
                .isPositive();
    }

    @Test
    @Order(2)
    @DisplayName("save Throws NotFoundException when codeFipe not found")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ThrowsNotFoundException_WhenCodeFipeNotFound() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var request = fileUtils.readResourceFile("/favorite/favorite-post-request-404.json");
        var expectedResponse = fileUtils.readResourceFile("/favorite/favorite-post-response-404.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .body(request)
                .when()
                .post("/v1/favorite")
                .then()
                .statusCode(HttpStatus.NOT_FOUND)
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(3)
    @DisplayName("findAllPaginated returns page of favorites 200 when successful")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllPaginated_ReturnsPageOfFavorites_WhenSuccessful() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("/favorite/get-favorite-find-all-paginated-response-200.json");

        var response = RestAssured.given()
                .auth().oauth2("Bearer " + adminToken)
                .accept(ContentType.JSON)
                .when()
                .get("/v1/favorite/paginated")
                .then()
                .statusCode(HttpStatus.OK)
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(4)
    @DisplayName("existsFavorite returns true 200 when favorite exists")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/add_two_favorites.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_favorites.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existsFavorite_ReturnsTrue_WhenFavoriteExists() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("/favorite/favorite-exists-get-response-200.json");

        var user = userRepository.findByEmail("admin@example.com").get();

        var favorite = favoriteRepository.findAllByUser(Pageable.ofSize(1), user)
                .getContent().getFirst();

        var response = RestAssured.given()
                .auth().oauth2("Bearer " + adminToken)
                .accept(ContentType.JSON)
                .when()
                .get("/v1/favorite?codeFipe=" + favorite.getVehicleData().getCodeFipe() + "&modelYear=" + favorite.getVehicleData().getModelYear() + "&fuelAcronym=" + favorite.getVehicleData().getFuel().getFuelAcronym())
                .then()
                .statusCode(HttpStatus.OK)
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Order(5)
    @DisplayName("existsFavorite returns false 200 when favorite not exists")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existsFavorite_ReturnsFalse_WhenFavoriteNotExists() throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("/favorite/favorite-not-exists-get-response-200.json");

        var user = userRepository.findByEmail("admin@example.com").get();

        var response = RestAssured.given()
                .auth().oauth2("Bearer " + adminToken)
                .accept(ContentType.JSON)
                .when()
                .get("/v1/favorite?codeFipe=" + "999999-9" + "&modelYear=" + "2999" + "&fuelAcronym=" + "Z")
                .then()
                .statusCode(HttpStatus.OK)
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response).isEqualTo(expectedResponse);
    }


    @ParameterizedTest
    @MethodSource("providedPostArguments")
    @Order(9)
    @DisplayName("save throws BadRequest when invalid fields")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ReturnsBadRequest_WhenInvalidFields(String requestPath, String expectedResponsePath) throws IOException {
        var adminToken = authUtils.login("/auth/post-auth-admin-request-200.json");
        var request = fileUtils.readResourceFile(requestPath);
        var expectedResponse = fileUtils.readResourceFile(expectedResponsePath);

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().oauth2("Bearer " + adminToken)
                .body(request)
                .when()
                .post("/v1/favorite")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST)
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> providedPostArguments() {
        return Stream.of(
                Arguments.of("/favorite/post-favorite-empty-fields-request-400.json", "/favorite/post-favorite-empty-fields-response-400.json"),
                Arguments.of("/favorite/post-favorite-blank-fields-request-400.json", "/favorite/post-favorite-blank-fields-response-400.json")
        );
    }
}