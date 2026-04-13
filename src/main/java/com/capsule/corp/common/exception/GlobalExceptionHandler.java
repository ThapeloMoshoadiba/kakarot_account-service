package com.capsule.corp.common.exception;

import static com.capsule.corp.infrastructure.http.resources.Constants.DATABASE_ERROR_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_REQUEST_MESSAGE;

import com.capsule.corp.infrastructure.http.controllers.GlobalErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<?> handleAccountNotFoundException(final AccountNotFoundException ex) {
    log.error("AccountNotFoundException:", ex);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(GlobalErrorResponse.builder().reason(ex.getMessage()).build());
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<?> handleClientNotFoundException(final ClientNotFoundException ex) {
    log.error("ClientNotFoundException:", ex);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(GlobalErrorResponse.builder().reason(ex.getMessage()).build());
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<?> handleBadRequestException(final Exception ex) {
    log.error("BadRequestException:", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(GlobalErrorResponse.builder().reason(INVALID_REQUEST_MESSAGE).build());
  }

  @ExceptionHandler({
    DataException.class,
    PSQLException.class,
    DataIntegrityViolationException.class
  })
  public ResponseEntity<?> handleDatabaseException(final Exception ex) {
    log.error("DatabaseException:", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(GlobalErrorResponse.builder().reason(DATABASE_ERROR_MESSAGE).build());
  }

  @ExceptionHandler(InvalidUpdateException.class)
  public ResponseEntity<?> handleInvalidUpdateException(final InvalidUpdateException ex) {
    log.error("InvalidUpdateException:", ex);
    return ResponseEntity.status(HttpStatus.OK)
        .body(GlobalErrorResponse.builder().reason(ex.getMessage()).build());
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<?> handleBusinessRuleException(final BusinessRuleException ex) {
    log.error("BusinessRuleException:", ex);
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .body(GlobalErrorResponse.builder().reason(ex.getMessage()).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGenericException(final Exception ex) {
    log.error("Exception:", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(GlobalErrorResponse.builder().reason(ex.getMessage()).build());
  }
}
