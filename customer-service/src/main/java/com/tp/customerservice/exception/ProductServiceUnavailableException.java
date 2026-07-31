package com.tp.customerservice.exception;

public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException(String message) {
        super(message);
    }
}
