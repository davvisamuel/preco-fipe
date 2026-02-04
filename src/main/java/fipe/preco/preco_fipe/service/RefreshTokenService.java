package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.RefreshToken;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.RefreshTokenPostResponse;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.RefreshTokenRepository;
import fipe.preco.preco_fipe.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${security.jwt.refreshExpirationDays}")
    private Long refreshTokenDurationDays;

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    public RefreshToken createRefreshToken(User user) {
        var refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenDurationDays).toInstant(ZoneOffset.of("-03:00")))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public RefreshTokenPostResponse refreshToken(String token) {
        var refreshTokenEntity = findByToken(token);

        var user = refreshTokenEntity.getUser();

        refreshTokenRepository.delete(refreshTokenEntity);

        if (isTokenExpired(refreshTokenEntity))
            throw new NotFoundException("Refresh token expired. Please login again.");

        var newRefreshToken = createRefreshToken(user).getToken();

        var jwtToken = tokenService.generationToken(user);

        return RefreshTokenPostResponse.builder()
                .refreshToken(newRefreshToken)
                .token(jwtToken)
                .build();
    }

    public void delete(String token) {
        var refreshTokenEntity = findByToken(token);

        refreshTokenRepository.delete(refreshTokenEntity);
    }

    public boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Invalid refresh token"));
    }
}
