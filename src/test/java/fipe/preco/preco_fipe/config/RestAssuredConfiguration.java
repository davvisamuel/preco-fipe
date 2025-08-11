package fipe.preco.preco_fipe.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@TestConfiguration
@Lazy
public class RestAssuredConfiguration {

    @LocalServerPort
    int port;

    @Bean
    public RequestSpecification requestSpecification() {
        return RestAssured
                .given()
                .baseUri("http://localhost:" + port);
    }
}
