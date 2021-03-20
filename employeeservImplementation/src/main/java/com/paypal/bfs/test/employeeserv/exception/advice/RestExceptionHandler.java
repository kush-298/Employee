package com.paypal.bfs.test.employeeserv.exception.advice;


import com.paypal.bfs.test.employeeserv.exception.ActionNotAllowedException;
import com.paypal.bfs.test.employeeserv.exception.DuplicateEntityException;
import com.paypal.bfs.test.employeeserv.exception.EntityNotFoundException;
import com.paypal.bfs.test.employeeserv.exception.InternalServerException;
import com.paypal.bfs.test.employeeserv.exception.model.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        this.logError(ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        ErrorMessage errorMessage = new ErrorMessage(fieldError.getDefaultMessage());
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorMessage handleEntityNotFoundException(Exception ex) {
        this.logError(ex);
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler({IOException.class, InternalServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorMessage handleIOException(Exception ex) {
        this.logError(ex);
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler({ActionNotAllowedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleEntityValidationException(Exception ex) {
        this.logError(ex);
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final ErrorMessage handleDuplicateEntityException(Exception ex) {
        this.logError(ex);
        return new ErrorMessage(ex.getMessage());
    }

    private void logError(Exception ex) {
        logger.error("Exception in REST API", ex);
    }

}
