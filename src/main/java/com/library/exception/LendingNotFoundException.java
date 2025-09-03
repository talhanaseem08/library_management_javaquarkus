package com.library.exception;

public class LendingNotFoundException extends RuntimeException {
    
    public LendingNotFoundException(String message) {
        super(message);
    }
    
    public LendingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
