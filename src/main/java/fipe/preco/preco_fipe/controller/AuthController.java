package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.LoginPostRequest;
import fipe.preco.preco_fipe.dto.request.RefreshTokenDeleteRequest;
import fipe.preco.preco_fipe.dto.request.RefreshTokenPostRequest;
import fipe.preco.preco_fipe.dto.request.RefreshTokenPostResponse;
import fipe.preco.preco_fipe.dto.response.LoginPostResponse;
import fipe.preco.preco_fipe.exception.ApiError;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.security.TokenService;
import fipe.preco.preco_fipe.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Auth", description = "Manage user's authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginPostResponse.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),

            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorMessage.class)))
    })
    public ResponseEntity<LoginPostResponse> login(@RequestBody @Valid LoginPostRequest loginPostRequest) {
        log.debug("Request received for '{}'", loginPostRequest);

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginPostRequest.getEmail(), loginPostRequest.getPassword());

        var auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        var user = (User) auth.getPrincipal();

        var token = tokenService.generationToken(user);

        var refreshToken = refreshTokenService.createRefreshToken(user);

        var loginPostResponse = LoginPostResponse.builder()
                .refreshToken(refreshToken.getToken())
                .token(token)
                .build();

        return ResponseEntity.ok(loginPostResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenPostResponse> refreshToken(@RequestBody @Valid RefreshTokenPostRequest request) {
        var refreshToken = request.refreshToken();

        var refreshTokenPostResponse = refreshTokenService.refreshToken(refreshToken);

        return ResponseEntity.ok(refreshTokenPostResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenDeleteRequest request) {
        var token = request.refreshToken();

        refreshTokenService.delete(token);

        return ResponseEntity.noContent().build();
    }
}
