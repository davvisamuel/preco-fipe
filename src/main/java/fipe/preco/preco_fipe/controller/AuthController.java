package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.request.LoginPostRequest;
import fipe.preco.preco_fipe.response.LoginPostResponse;
import fipe.preco.preco_fipe.security.TokenService;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
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
