package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.User;

import java.time.LocalDateTime;

public class ComparisonUtils {

    public static Comparison newComparisonToSave(User user) {

        return Comparison.builder()
                .user(user)
                .build();
    }

    public static Comparison newComparisonSaved(User user) {

        return Comparison.builder()
                .id(1)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
