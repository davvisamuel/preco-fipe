package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Role;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static List<User> newUserList() {
        var user1 = User.builder()
                .id(1)
                .email("test1@gmail.com")
                .password("test1")
                .role(Role.USER)
                .build();

        var user2 = User.builder()
                .id(2)
                .email("test2@outlook.com")
                .password("test2")
                .role(Role.USER)
                .build();

        var user3 = User.builder()
                .id(3)
                .email("test3@gmail.com")
                .password("test3")
                .role(Role.USER)
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
                .password(passwordEncoder.encode(rawPasswordOfSavedUser()))
                .role(Role.USER)
                .build();
    }

    public static String rawPasswordOfSavedUser() {
        return "test1";
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
