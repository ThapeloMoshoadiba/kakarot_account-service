package com.capsule.corp.common.exception;

public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(String message, String accountNumber) {
    super(message);
  }
}
