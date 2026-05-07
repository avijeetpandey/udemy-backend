package com.avijeet.udemybackend.exceptions.profile;

public class ProfileNotUpdatedException extends RuntimeException {
    public ProfileNotUpdatedException(String message) {
        super(message);
    }
}
