package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final FileUtils fileUtils;

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

    public List<User> newUserList() {
        var user1 = User.builder()
                .id(1)
                .email("test1@gmail.com")
                .password("test1")
                .roles("USER")
                .build();

        var user2 = User.builder()
                .id(2)
                .email("test2@outlook.com")
                .password("test2")
                .roles("USER")
                .build();

        var user3 = User.builder()
                .id(3)
                .email("test3@gmail.com")
                .password("test3")
                .roles("USER")
                .build();

        return new ArrayList<>(List.of(user1, user2, user3));
    }

    public User newUserToSave() {
        return User.builder()
                .email("test1@gmail.com")
                .password("test1")
                .build();
    }

    public User newSavedUser() {
        return User.builder()
                .id(4)
                .email("test1@gmail.com")
                .password("$2a$10$QvLzMZj0H3V7Z6p2Zv8F/O3FzXKkF2xY5Hq0jM2FsC3v1P7U7k0lO")
                .roles("USER")
                .build();
    }

    public User newUserToUpdate() {
        return User.builder()
                .email("updated@gmail.com")
                .password("updated")
                .build();
    }
}
