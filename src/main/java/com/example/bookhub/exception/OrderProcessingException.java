package com.example.bookhub.exception;

public class OrderProcessingException extends RuntimeException {
  public OrderProcessingException(String message) {
    super(message);
  }
}
