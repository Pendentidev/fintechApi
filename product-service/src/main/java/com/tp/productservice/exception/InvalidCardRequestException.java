package com.tp.productservice.exception;

public class InvalidCardRequestException extends RuntimeException {
    public InvalidCardRequestException(String message) {
        super(message);
    }
}
