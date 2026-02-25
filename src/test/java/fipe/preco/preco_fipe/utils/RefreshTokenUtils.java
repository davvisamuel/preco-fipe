package fipe.preco.preco_fipe.utils;

import fipe.preco.preco_fipe.domain.RefreshToken;
import fipe.preco.preco_fipe.domain.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class RefreshTokenUtils {

    public static final Long REFRESH_TOKEN_EXPIRATION_DAYS = 7L;

    public static RefreshToken newRefreshToken(User user) {
        var refreshToken = user.getOrCreateRefreshToken();

        refreshToken.rotate(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS).toInstant(ZoneOffset.of("-03:00"))
        );

        return refreshToken;
    }

    public static RefreshToken newRefreshTokenExpired(User user) {
        var refreshToken = user.getOrCreateRefreshToken();

        refreshToken.rotate(
                UUID.randomUUID().toString(),
                LocalDateTime.now().minusDays(REFRESH_TOKEN_EXPIRATION_DAYS).toInstant(ZoneOffset.of("-03:00"))
        );

        return refreshToken;

    }
}
