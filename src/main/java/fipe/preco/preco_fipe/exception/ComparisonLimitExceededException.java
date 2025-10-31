package fipe.preco.preco_fipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ComparisonLimitExceededException extends ResponseStatusException {

    public ComparisonLimitExceededException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
