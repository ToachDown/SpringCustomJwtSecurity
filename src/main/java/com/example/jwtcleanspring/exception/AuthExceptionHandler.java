package com.example.jwtcleanspring.exception;

import com.example.jwtcleanspring.exception.exceptions.InvalidLoginException;
import com.example.jwtcleanspring.exception.exceptions.JwtHandlerException;
import com.example.jwtcleanspring.exception.exceptions.NoPermissionException;
import com.example.jwtcleanspring.exception.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handlerInvalidLogin(InvalidLoginException invalidLoginException) {
        return new ResponseEntity<>(invalidLoginException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<String> handlerNoPermission(NoPermissionException noPermissionException) {
        return new ResponseEntity<>(noPermissionException.getMessage(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handlerUnauthorized(UnauthorizedException unauthorizedException) {
        return new ResponseEntity<>(unauthorizedException.getMessage(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtHandlerException.class)
    public ResponseEntity<String> handlerUnauthorized(JwtHandlerException jwtHandlerException) {
        return new ResponseEntity<>(jwtHandlerException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArguments(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity<>(illegalArgumentException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
