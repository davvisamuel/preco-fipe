package fipe.preco.preco_fipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {
    public EmailAlreadyExistsException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
