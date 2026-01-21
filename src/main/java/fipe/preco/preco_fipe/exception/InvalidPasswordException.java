package fipe.preco.preco_fipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPasswordException extends ResponseStatusException {

    public InvalidPasswordException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
