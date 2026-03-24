package com.capsule.corp.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<?> handleAccountNotFoundException(final AccountNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<?> handleBusinessRuleException(final BusinessRuleException ex) {
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<?> handleClientNotFoundException(final ClientNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGenericException(final Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }
}
