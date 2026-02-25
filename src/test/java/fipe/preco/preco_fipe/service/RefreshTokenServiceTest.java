package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.config.RefreshTokenProperties;
import fipe.preco.preco_fipe.domain.RefreshToken;
import fipe.preco.preco_fipe.exception.BadRequestException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.RefreshTokenRepository;
import fipe.preco.preco_fipe.security.TokenService;
import fipe.preco.preco_fipe.utils.RefreshTokenUtils;
import fipe.preco.preco_fipe.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private RefreshTokenProperties refreshTokenProperties;

    @Test
    @Order(1)
    @DisplayName("createRefreshToken returns RefreshToken when user is valid")
    void createRefreshToken_ReturnsRefreshToken_WhenSuccessful() {
        var user = UserUtils.newSavedUser();

        var expiryDate = LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));

        BDDMockito.when(refreshTokenProperties.getExpiryDate())
                .thenReturn(expiryDate);

        BDDMockito.when(refreshTokenRepository.save(ArgumentMatchers.any(RefreshToken.class)))
                .then(invocation -> invocation.getArgument(0));

        var refreshToken = refreshTokenService.createRefreshToken(user);

        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshToken.getUser()).isEqualTo(user);
        Assertions.assertThat(refreshToken.getToken()).isNotBlank();
        Assertions.assertThat(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
    }

    @Test
    @Order(2)
    @DisplayName("refreshToken generates new JWT and refresh token when refresh token is valid")
    void refreshToken_RenewJwtToken_WhenRefreshTokenIsValid() {
        var user = UserUtils.newSavedUser();

        var refreshToken = RefreshTokenUtils.newRefreshToken(user);

        BDDMockito.when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));

        var tokenJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHVzZXIuY29tIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjQ3MDAwMDAwMDB9.fake-signature";

        BDDMockito.when(tokenService.generationToken(user)).thenReturn(tokenJwt);

        var refreshTokenPostResponse = refreshTokenService.refreshToken(refreshToken.getToken());

        Assertions.assertThat(refreshTokenPostResponse)
                .isNotNull()
                .hasNoNullFieldsOrProperties();

        Assertions.assertThat(refreshTokenPostResponse.token()).isEqualTo(tokenJwt);
    }

    @Test
    @Order(3)
    @DisplayName("refreshToken throws NotFoundException when refresh token is expired")
    void refreshToken_ThrowsBadRequest_WhenRefreshTokenExpired() {
        var user = UserUtils.newSavedUser();

        var refreshTokenExpired = RefreshTokenUtils.newRefreshTokenExpired(user);

        BDDMockito.when(refreshTokenRepository.findByToken(refreshTokenExpired.getToken()))
                .thenReturn(Optional.of(refreshTokenExpired));

        BDDMockito.doNothing().when(refreshTokenRepository).delete(refreshTokenExpired);

        Assertions.assertThatException().isThrownBy(() -> refreshTokenService.refreshToken(refreshTokenExpired.getToken()))
                .isInstanceOf(BadRequestException.class);
    }


    @Test
    @Order(4)
    @DisplayName("delete removes refresh token when token is found")
    void delete_RemovesRefreshToken_WhenRefreshTokenIsFound() {
        var user = UserUtils.newSavedUser();

        var refreshToken = RefreshTokenUtils.newRefreshToken(user);

        BDDMockito.when(refreshTokenRepository.findByToken(refreshToken.getToken()))
                .thenReturn(Optional.of(refreshToken));

        Assertions.assertThatNoException().isThrownBy(() -> refreshTokenService.delete(refreshToken.getToken()));
    }

    @Test
    @Order(5)
    @DisplayName("findByToken returns refresh token when token exists")
    void findByToken_ReturnsRefreshToken_WhenRefreshTokenIsFound() {
        var user = UserUtils.newSavedUser();

        var expectedRefreshToken = RefreshTokenUtils.newRefreshToken(user);

        var token = expectedRefreshToken.getToken();

        BDDMockito.when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(expectedRefreshToken));

        var refreshToken = refreshTokenService.findByToken(token);

        Assertions.assertThat(refreshToken)
                .isNotNull()
                .isEqualTo(expectedRefreshToken);
    }

    @Test
    @Order(6)
    @DisplayName("findByToken throws NotFoundException when token does not exist")
    void findByToken_ReturnsRefreshToken_WhenRefreshTokenNotFound() {
        var token = UUID.randomUUID().toString();

        BDDMockito.when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> refreshTokenService.findByToken(token))
                .isInstanceOf(NotFoundException.class);
    }
}