package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.LoginPostRequest;
import fipe.preco.preco_fipe.dto.response.LoginPostResponse;
import fipe.preco.preco_fipe.exception.ApiError;
import fipe.preco.preco_fipe.exception.DefaultErrorMessage;
import fipe.preco.preco_fipe.security.TokenService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Auth", description = "Manage user's authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

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

        var token = tokenService.generationToken((User) auth.getPrincipal());

        var loginPostResponse = LoginPostResponse.builder()
                .token(token)
                .build();

        return ResponseEntity.ok(loginPostResponse);
    }
}
