package pl.createcompetition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =HttpStatus.CONFLICT, reason="Login not exist")
public class LoginNotExistException extends RuntimeException{
}
