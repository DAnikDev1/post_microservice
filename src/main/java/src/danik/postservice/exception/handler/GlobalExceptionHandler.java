package src.danik.postservice.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import src.danik.postservice.exception.DataValidationException;
import src.danik.postservice.exception.ExistedEntityException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse processEntityNotFoundException(EntityNotFoundException e) {
        return ErrorResponse.builder().message(e.getMessage()).build();
    }

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse processBadRequestException(DataValidationException e) {
        return ErrorResponse.builder().message(e.getMessage()).build();
    }

}
