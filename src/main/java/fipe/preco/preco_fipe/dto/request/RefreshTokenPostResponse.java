package fipe.preco.preco_fipe.dto.request;

import lombok.Builder;

@Builder
public record RefreshTokenPostResponse(String refreshToken, String token) {
}
