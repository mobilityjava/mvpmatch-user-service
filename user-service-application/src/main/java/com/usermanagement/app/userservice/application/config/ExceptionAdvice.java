package com.usermanagement.app.userservice.application.config;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  public static final String INTERNAL_SERVER_ERROR_MSG = "500 - internal server error";

  @ExceptionHandler(value = {RuntimeException.class})
  protected ResponseEntity<Object> handleError(RuntimeException ex, WebRequest request) {

    if (ex instanceof ResponseStatusException) {
      if (((ResponseStatusException) ex).getStatus().is5xxServerError()) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
      } else {
        log.warn("Operation failed for user: {}", ex.getMessage(), ex);
      }

      // keep original status
      throw ex;
    }
    log.error("An error occurred: {}", ex.getMessage(), ex);
    return handleExceptionInternal(ex, INTERNAL_SERVER_ERROR_MSG,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleValidation(ConstraintViolationException ex,
      WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(),
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleValidation(IllegalArgumentException ex,
      WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(),
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

}
