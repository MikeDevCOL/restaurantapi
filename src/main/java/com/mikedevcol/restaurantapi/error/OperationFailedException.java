package com.mikedevcol.restaurantapi.error;

public class OperationFailedException extends RuntimeException {

  public OperationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
