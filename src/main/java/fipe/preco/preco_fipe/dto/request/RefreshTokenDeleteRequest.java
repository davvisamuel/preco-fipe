package fipe.preco.preco_fipe.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDeleteRequest(
        @NotBlank(message = "The field 'refreshToken' is required")
        String refreshToken) {
}
