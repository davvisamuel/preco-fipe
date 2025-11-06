package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.config.RestAssuredConfiguration;
import fipe.preco.preco_fipe.config.TestcontainersConfiguration;
import fipe.preco.preco_fipe.repository.ConsultationRepository;
import fipe.preco.preco_fipe.repository.VehicleDataRepository;
import fipe.preco.preco_fipe.utils.AuthUtils;
import fipe.preco.preco_fipe.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/fipe-api", stubs = "classpath:/wiremock/fipe-api/mappings")
@Log4j2
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
class FipeApiControllerIT {
    private static final String BASE_URL = "v1/api";
    private static final String BRANDS_BY_VEHICLE_TYPE_URI = "/{vehicleType}";
    private static final String MODELS_BY_BRAND_ID_URI = "/{vehicleType}/brands/{brandId}/models";
    private static final String YEARS_BY_MODEL_ID_URI = "/{vehicleType}/brands/{brandId}/models/{modelId}/years";
    private static final String FIPE_INFORMATION_URI = "/{vehicleType}/brands/{brandId}/models/{modelId}/years/{yearId}";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private RequestSpecification requestSpecification;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private VehicleDataRepository vehicleDataRepository;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("findAllBrandsByType returns the full list of brands when successful")
    @Order(1)
    void findAllBrandsByType_ReturnsAllBrandsList_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-find-all-brands-by-type-response-200.json");

        var vehicleType = "cars";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + BRANDS_BY_VEHICLE_TYPE_URI, vehicleType)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("findAllModelsByBrandId returns the full list of models when brand is found")
    @Order(2)
    void findAllModelsByBrandId_ReturnsAllModelsList_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-find-all-models-by-brand-id-response-200.json");

        var vehicleType = "cars";
        var brandId = "1";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + MODELS_BY_BRAND_ID_URI, vehicleType, brandId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("findAllModelsByBrandId ThrowsNotFoundException when brand is not found")
    @Order(3)
    void findAllModelsByBrandId_ThrowsNotFoundException_WhenBrandIsNotFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-find-all-models-by-brand-id-response-404.json");

        var vehicleType = "cars";
        var brandId = "999";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + MODELS_BY_BRAND_ID_URI, vehicleType, brandId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("findAllYearsByModelId returns the full list of years when model is found")
    @Order(4)
    void findAllYearsByModelId_ReturnsAllYearsList_WhenModelIsFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-find-all-years-by-model-id-response-200.json");

        var vehicleType = "cars";
        var brandId = "1";
        var modelId = "1";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + YEARS_BY_MODEL_ID_URI, vehicleType, brandId, modelId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("findAllYearsByModelId ThrowsNotFoundException when model is not found")
    @Order(5)
    void findAllYearsByModelId_ThrowsNotFoundException_WhenModelIsNotFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-find-all-years-by-model-id-response-404.json");

        var vehicleType = "cars";
        var brandId = "1";
        var modelId = "999";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + YEARS_BY_MODEL_ID_URI, vehicleType, brandId, modelId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("retrieveFipeInformation returns the FIPE information when successful")
    @Order(6)
    void retrieveFipeInformation_ReturnsTheFipeInformation_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-fipe-information-response-200.json");

        var vehicleType = "cars";
        var brandId = 1;
        var modelId = 1;
        var yearId = "1992-1";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + FIPE_INFORMATION_URI, vehicleType, brandId, modelId, yearId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("retrieveFipeInformation throws a NotFoundException when the vehicle is not found")
    @Order(7)
    void retrieveFipeInformation_ThrowsNotFoundException_WhenVehicleIsNotFound() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-fipe-information-response-404.json");

        var vehicleType = "cars";
        var brandId = 1;
        var modelId = 1;
        var yearId = "9999-1";

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + FIPE_INFORMATION_URI, vehicleType, brandId, modelId, yearId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    /*
        Por causa da implementação do RabbitMQ esse texte precisa ser modificado
     */

//    @Test
//    @DisplayName("retrieveFipeInformation save the fipe information in the database when user is authenticated")
//    @Order(8)
//    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    void retrieveFipeInformation_SaveTheFipeInformation_WhenUserAuthenticated() throws IOException {
//        var expectedResponse = fileUtils.readResourceFile("/fipe-api/expected-get-fipe-information-response-200.json");
//        var userToken = authUtils.login("/auth/post-auth-admin-request-200.json");
//
//        var vehicleType = "cars";
//        var brandId = 1;
//        var modelId = 1;
//        var yearId = "1992-1";
//
//        RestAssured.given()
//                .accept(ContentType.JSON)
//                .contentType(ContentType.JSON)
//                .auth().oauth2("Bearer " + userToken)
//                .when()
//                .get(BASE_URL + FIPE_INFORMATION_URI, vehicleType, brandId, modelId, yearId)
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body(Matchers.equalTo(expectedResponse))
//                .log().all();
//
//        var consultation = consultationRepository.findAll().getFirst();
//        Assertions.assertThat(consultation).hasNoNullFieldsOrProperties();
//
//        var vehicleData = vehicleDataRepository.findAll().getFirst();
//        Assertions.assertThat(consultation.getVehicleData().getId()).isEqualTo(vehicleData.getId());
//    }

}