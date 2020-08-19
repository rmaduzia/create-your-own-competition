package pl.createcompetition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Competition Name Already Exists")
public class CompetitionExistsException extends RuntimeException{
}
