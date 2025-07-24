package com.example.loyalty.exceptions;

public class ReceiptProcessingException extends RuntimeException {
    public ReceiptProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
