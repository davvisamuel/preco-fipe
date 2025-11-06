package fipe.preco.preco_fipe.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    public final FileUtils fileUtils;

    public String login(String filePathRequest) throws IOException {
        var request = fileUtils.readResourceFile(filePathRequest);

        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/auth/login")
                .then()
                .extract()
                .path("token");
    }

}
