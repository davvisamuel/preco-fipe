package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.config.RestAssuredConfiguration;
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
import org.springframework.http.HttpStatus;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/fipe-api", stubs = "classpath:/wiremock/fipe-api/mappings")
@Log4j2
class PrecoFipeControllerIT {
    private static final String BASE_URL = "v1/api";
    private static final String BRANDS_BY_VEHICLE_TYPE_URI = "/{vehicleType}";
    private static final String MODELS_BY_BRAND_ID_URI = "/{vehicleType}/brands/{brandId}/models";
    private static final String YEARS_BY_MODEL_ID_URI = "/{vehicleType}/brands/{brandId}/models";
    private static final String FIPE_INFORMATION_URI = "/{vehicleType}/brands/{brandId}/models/{modelId}/years/{yearId}";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private RequestSpecification requestSpecification;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("retrieveFipeInformation returns the FIPE information when successful.")
    @Order(1)
    void retrieveFipeInformation_ReturnsTheFipeInformation_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceFile("/fipe-api/fipe-information/expected-get-fipe-information-response-200.json");

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
}