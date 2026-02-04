package fipe.preco.preco_fipe.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenPostRequest(
        @NotBlank(message = "The field 'refreshToken' is required")
        String refreshToken) {
}
