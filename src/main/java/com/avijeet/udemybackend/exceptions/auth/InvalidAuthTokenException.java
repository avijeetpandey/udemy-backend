package com.avijeet.udemybackend.exceptions.auth;

public class InvalidAuthTokenException extends RuntimeException {
    public InvalidAuthTokenException(String message) {
        super(message);
    }
}
