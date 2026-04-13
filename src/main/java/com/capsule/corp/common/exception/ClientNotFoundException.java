package com.capsule.corp.common.exception;

public class ClientNotFoundException extends RuntimeException {
  public ClientNotFoundException(final String message) {
    super(message);
  }
}
