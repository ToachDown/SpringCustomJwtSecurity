package com.example.jwtcleanspring.exception.exceptions;

public class JwtHandlerException extends RuntimeException{

    public JwtHandlerException() {
    }

    public JwtHandlerException(String message) {
        super(message);
    }

    public JwtHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtHandlerException(Throwable cause) {
        super(cause);
    }
}
