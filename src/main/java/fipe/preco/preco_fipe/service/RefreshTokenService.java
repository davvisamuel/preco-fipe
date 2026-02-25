package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.config.RefreshTokenProperties;
import fipe.preco.preco_fipe.domain.RefreshToken;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.RefreshTokenPostResponse;
import fipe.preco.preco_fipe.exception.BadRequestException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.RefreshTokenRepository;
import fipe.preco.preco_fipe.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenProperties refreshTokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        var refreshToken = user.getOrCreateRefreshToken();

        refreshToken.rotate(UUID.randomUUID().toString(), refreshTokenProperties.getExpiryDate());

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshTokenPostResponse refreshToken(String token) {
        var refreshToken = findByToken(token);

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expired. Please login again.");
        }

        refreshToken.rotate(UUID.randomUUID().toString(), refreshTokenProperties.getExpiryDate());

        var jwtToken = tokenService.generationToken(refreshToken.getUser());

        return RefreshTokenPostResponse.builder()
                .refreshToken(refreshToken.getToken())
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void delete(String token) {
        var refreshToken = findByToken(token);

        refreshToken.getUser().setRefreshToken(null);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Invalid refresh token"));
    }
}
