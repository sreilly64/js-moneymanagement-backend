package com.jszipcoders.moneymanager.exceptions;

import com.jszipcoders.moneymanager.models.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    private static final String ERROR = "Error Details - {}";

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleEntityNotFound(EntityNotFoundException e){
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.error(ERROR, errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
