package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Role;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    public static List<User> newUserList() {
        var user1 = User.builder()
                .id(1)
                .email("test1@gmail.com")
                .password("test1")
                .role(Role.USER.name())
                .build();

        var user2 = User.builder()
                .id(2)
                .email("test2@outlook.com")
                .password("test2")
                .role(Role.USER.name())
                .build();

        var user3 = User.builder()
                .id(3)
                .email("test3@gmail.com")
                .password("test3")
                .role(Role.USER.name())
                .build();

        return new ArrayList<>(List.of(user1, user2, user3));
    }

    public static User newUserToSave() {
        return User.builder()
                .email("test1@gmail.com")
                .password("test1")
                .build();
    }

    public static User newSavedUser() {
        return User.builder()
                .id(4)
                .email("test1@gmail.com")
                .password("$2a$10$QvLzMZj0H3V7Z6p2Zv8F/O3FzXKkF2xY5Hq0jM2FsC3v1P7U7k0lO")
                .role(Role.USER.name())
                .build();
    }

    public static User newUserToUpdate() {
        return User.builder()
                .email("updated@gmail.com")
                .password("updated")
                .build();
    }

    public static Page<User> newPageUser() {
        var pageable = Pageable.ofSize(3);
        return new PageImpl<>(newUserList(), pageable, pageable.getPageSize());
    }
}
