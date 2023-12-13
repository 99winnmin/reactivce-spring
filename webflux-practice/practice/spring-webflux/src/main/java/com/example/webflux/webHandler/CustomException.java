package com.example.webflux.webHandler;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}