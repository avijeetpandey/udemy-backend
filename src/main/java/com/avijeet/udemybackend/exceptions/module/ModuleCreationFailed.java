package com.avijeet.udemybackend.exceptions.module;

public class ModuleCreationFailed extends RuntimeException {
    public ModuleCreationFailed(String message) {
        super(message);
    }
}
