package fipe.preco.preco_fipe.exception;

import lombok.Builder;

@Builder
public record ApiError(String timestamp, int status, String error, String message, String path) {
}
