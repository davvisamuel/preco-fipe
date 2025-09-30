package fipe.preco.preco_fipe.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import fipe.preco.preco_fipe.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${security.jwt.secret}")
    private String secret;

    public String generationToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var token = JWT.create()
                    .withIssuer("preco-fipe")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(genExpiresAt())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateLogin(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var subject = JWT.require(algorithm)
                    .withIssuer("preco-fipe")
                    .build()
                    .verify(token)
                    .getSubject();

            return subject;
        } catch (JWTCreationException e) {
            return "";
        }
    }

    public Instant genExpiresAt() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
