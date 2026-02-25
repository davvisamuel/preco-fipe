package fipe.preco.preco_fipe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ConfigurationProperties(prefix = "security.refresh-token")
public record RefreshTokenProperties(Long refreshExpirationDays) {

    public Instant getExpiryDate() {
        return LocalDateTime.now().plusDays(refreshExpirationDays).toInstant(ZoneOffset.of("-03:00"));
    }
}
