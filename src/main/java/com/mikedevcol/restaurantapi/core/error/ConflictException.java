package com.mikedevcol.restaurantapi.core.error;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
}
