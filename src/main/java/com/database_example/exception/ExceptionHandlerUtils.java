package com.database_example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerUtils extends ResponseEntityExceptionHandler {

    @ExceptionHandler(APIException.class)
    public final ResponseEntity<?> exceptionOccur(APIException exception) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .errors(exception.getCause().getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(exceptionDTO);
    }
}

