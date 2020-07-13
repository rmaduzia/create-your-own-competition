package pl.createcompetition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Login already exist")
public class DuplicateLoginException extends RuntimeException{
}
