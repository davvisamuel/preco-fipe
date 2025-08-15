package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        var user1 = User.builder()
                .id(1L)
                .email("test1@gmail.com")
                .password("test1")
                .roles("USER")
                .build();

        var user2 = User.builder()
                .id(2L)
                .email("test2@outlook.com")
                .password("test2")
                .roles("USER")
                .build();

        var user3 = User.builder()
                .id(3L)
                .email("test3@gmail.com")
                .password("test3")
                .roles("ADMIN")
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
                .id(1L)
                .email("test1@gmail.com")
                .password("test1")
                .roles("USER")
                .build();
    }

    public User newUserToUpdate() {
        return User.builder()
                .id(1L)
                .email("updated@gmail.com")
                .password("updated")
                .build();
    }
}
